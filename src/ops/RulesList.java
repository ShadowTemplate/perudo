package ops;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import miscellanea.Constants;

public class RulesList {

    private static Rule[] rules;

    static {
        rules = new Rule[21];
        rules[0] = new Rule("(defglobal ?*bluffThreshold* = 0)", "101");
        rules[1] = new Rule("(defglobal ?*reliabilityThreshold* = 0)", "101");
        rules[2] = new Rule("(deftemplate matchStatus (slot situation))", "101");
        rules[3] = new Rule("(deftemplate bet (slot face) (slot times) (slot probability) (slot bluff))", "101");
        rules[4] = new Rule("(deftemplate possibleBet (slot face) (slot times) (slot score))", "101");
        rules[5] = new Rule("(assert (perform search))", "101");
        rules[6] = new Rule("(assert (determine suggestion))", "101");
        rules[7] = new Rule("(defrule establishSituation (declare (salience SALIENCE)) => (bind ?sit (call Core getSituation)) (assert (matchStatus (situation ?sit))) (printout STREAM_NAME \"Game situation: \" ?sit crlf))", "100");
        rules[8] = new Rule("(defrule setBluffTreshold (declare (salience SALIENCE)) => (bind ?*bluffThreshold* (call Core getBluffThreshold)) (printout STREAM_NAME \"Bluff threshold set to: \" ?*bluffThreshold* crlf))", "100");
        rules[9] = new Rule("(defrule setAdmissibilityTreshold (declare (salience SALIENCE)) => (bind ?*reliabilityThreshold* (call Core getReliabilityThreshold)) (printout STREAM_NAME \"Reliability threshold set to: \" ?*reliabilityThreshold* crlf))", "100");
        rules[10] = new Rule("(defrule dudoBetEvaluation (declare (salience SALIENCE)) (test (not (call Core isFirstBet))) => (bind ?pDudo (call Core pDudo)) (assert (bet (face dudo) (times 0) (probability ?pDudo) (bluff 0))) (printout STREAM_NAME \"Dudo probability: \" ?pDudo crlf))", "100");
        rules[11] = new Rule("(defrule checkEndOfSearch (declare (salience SALIENCE)) (bet (face dudo) (times 0) (probability ?value) (bluff 0)) (test (eq 1 ?value)) ?search <- (perform search) => (retract ?search) (printout STREAM_NAME \"There is no need to calculate the probability of any bet. Dudo probability is 1.\" crlf))", "99");
        rules[12] = new Rule("(defrule fixReliabilityThreshold (declare (salience SALIENCE)) (test (call Core hasOneDie)) => (bind ?*reliabilityThreshold* (call Core fixReliabilityThreshold)) (printout STREAM_NAME \"You only have one die. Reliability threshold automatically increased to: \" ?*reliabilityThreshold* crlf) (undefrule fixReliabilityThreshold))", "99");
        rules[13] = new Rule("(defrule fixDudoProbability (declare (salience SALIENCE)) (test (eq (call Core isAnibodyPalifico) TRUE)) ?a <- (bet (face dudo) (times ?) (probability ?) (bluff ?)) => (bind ?newPDudo (call Core fixDudoProbability)) (retract ?a) (assert (bet (face dudo) (times 0) (probability ?newPDudo) (bluff 0))) (printout STREAM_NAME \"A player is palifico, so there will be forced bet on not owned dice. It is better to doubt with higher probability. Dudo probability increased to: \" ?newPDudo crlf) (undefrule fixDudoProbability))", "99");
        rules[14] = new Rule("(defrule getBetsForNormalSituation (declare (salience SALIENCE)) (matchStatus (situation \"NORMAL_SITUATION\")) (perform search) (test (> (fetch len) 0)) => (printout STREAM_NAME \"Started calculating bets for normal situation.\" crlf) (bind ?i 0) (while (< ?i (fetch len)) (bind ?currentElement (call (fetch keyList) get ?i)) (bind ?currFace (call ?currentElement toOPSString)) (bind ?min (call (fetch minValues) get ?currentElement)) (bind ?max (call Core getUpperBoundRangeLimitForBets ?min)) (while (<= ?min ?max) (bind ?bluffVal (call Core estimateBluffForBet nil ?min ?currentElement)) (if (call Core isAnibodyPalifico) then (bind ?probVal (call Core pAtLeastNTimesFaceForPlayer nil ?currentElement ?min)) (assert (bet (face ?currFace) (times ?min) (probability ?probVal) (bluff ?bluffVal))) (printout STREAM_NAME \"Calculated bet (without L): at least \" ?min \" times Die \" ?currFace \" - P: \" ?probVal \" B: \" ?bluffVal crlf) else (bind ?probVal (call Core pAtLeastNTimesFaceOrLamaForPlayer nil ?currentElement ?min)) (assert (bet (face ?currFace) (times ?min) (probability ?probVal) (bluff ?bluffVal))) (printout STREAM_NAME \"Calculated bet: at least \" ?min \" times Die \" ?currFace \" - P: \" ?probVal \" B: \" ?bluffVal crlf)) (bind ?min (+ ?min 1))) (bind ?i (+ ?i 1))) (printout STREAM_NAME \"Finished calculating bets for normal situation.\" crlf) (undefrule getBetsForNormalSituation))", "98");
        rules[15] = new Rule("(defrule getBetsForFinalBlowSituation (declare (salience SALIENCE)) (matchStatus (situation \"FINAL_BLOW\")) (perform search) (test (> (fetch len) 0)) => (printout STREAM_NAME \"Started calculating bets for final blow situation.\" crlf) (bind ?suggestedBet (call Core getBetForFinalBlowSituation (fetch minValues))) (bind ?suggestedBetTimes (call ?suggestedBet getTimes)) (if (> ?suggestedBetTimes 0) then (assert (possibleBet (face (call (call ?suggestedBet getFace) toOPSString)) (times ?suggestedBetTimes) (score 0)))) (printout STREAM_NAME \"Finished calculating bets for final blow situation.\" crlf) (undefrule getBetsForFinalBlowSituation))", "98");
        rules[16] = new Rule("(defrule getBetsForLastBetSituation (declare (salience SALIENCE)) (matchStatus (situation \"LAST_BET\")) (perform search) (test (> (fetch len) 0)) => (printout STREAM_NAME \"Started calculating bets for last bet situation.\" crlf) (bind ?suggestedBet (call Core getBetForLastBetSituation (fetch minValues))) (bind ?suggestedBetTimes (call ?suggestedBet getTimes)) (if (> ?suggestedBetTimes 0) then (assert (possibleBet (face (call (call ?suggestedBet getFace) toOPSString)) (times ?suggestedBetTimes) (score 0)))) (printout STREAM_NAME \"Finished calculating bets for last bet situation.\" crlf) (undefrule getBetsForLastBetSituation))", "98");
        rules[17] = new Rule("(defrule quantifyScoresForBet (declare (salience SALIENCE)) ?actBet <- (bet (face ?fac) (times ?tim) (probability ?prob) (bluff ?blu)) => (printout STREAM_NAME \"Started calculating unreliability for a bet.\" crlf) (if (> ?tim 0) then (bind ?scoreVal (+ (abs (- ?*reliabilityThreshold* ?prob)) (abs (- ?*bluffThreshold* ?blu)))) (assert (possibleBet (face ?fac) (times ?tim) (score ?scoreVal))) (retract ?actBet) (printout STREAM_NAME \"Calculated unreliability of the bet at least \" ?tim \" times \" ?fac \": \" ?scoreVal crlf)) (printout STREAM_NAME \"Finished calculating unreliability for a bet.\" crlf))", "4");
        rules[18] = new Rule("(defrule chooseBestScore (declare (salience SALIENCE)) ?actBet <- (possibleBet (face ?fac) (times ?tim) (score ?score1)) ?actBet2 <- (possibleBet (face ?fac2) (times ?tim2) (score ?score2)) (test (or (neq ?fac ?fac2) (and (eq ?fac ?fac2) (neq ?tim ?tim2)))) => (if (> ?score1 ?score2) then (retract ?actBet) else (retract ?actBet2)))", "3");
        rules[19] = new Rule("(defrule determineBet (declare (salience SALIENCE)) ?perf <- (determine suggestion) ?bestBet <- (possibleBet (face ?fac) (times ?tim) (score ?score)) ?dudo <- (bet (face dudo) (times ?) (probability ?prob) (bluff ?)) => (if (> ?prob (- 1 ?score)) then (printout STREAM_NAME \"*SUGGESTED_BET: Dudo\" crlf) else (printout STREAM_NAME \"*SUGGESTED_BET: \" ?tim \" times Die \" ?fac crlf)) (retract ?perf) (printout STREAM_NAME \"Bet suggested.\" crlf))", "2");
        rules[20] = new Rule("(defrule determineBetWithoutDudo (declare (salience SALIENCE)) (determine suggestion) ?bestBet <- (possibleBet (face ?fac) (times ?tim) (score ?score)) => (printout STREAM_NAME \"*SUGGESTED_BET: \" ?tim \" times Die \" ?fac crlf) (printout STREAM_NAME \"Bet suggested.\" crlf))", "1");
    }

    public static Rule[] getRules() {
        return rules;
    }

    public static void setRules(Rule[] newRules) {
        rules = newRules;
    }

    public static String[] getEncodedRules() {
        String[] encodedRules = new String[rules.length];
        String sep = Constants.ENVIRONMENT_RULES_SEPERATOR;
        for (int i = 0; i < encodedRules.length; i++) {
            encodedRules[i] = rules[i].getRule() + sep
                    + rules[i].getSalience();
        }
        return encodedRules;
    }

    public static Rule[] parseRules() {
        ArrayList<Rule> parsedRules = new ArrayList<>();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(Constants.ENVIRONMENT_RULES_FILE_PATH));
            String buffer;
            String[] splits;
            while ((buffer = in.readLine()) != null) {
                splits = buffer.split(Constants.ENVIRONMENT_RULES_SEPERATOR);
                if (splits.length == 2) {
                    parsedRules.add(new Rule(splits[0], splits[1]));
                } else {
                    System.out.println("buf" + buffer);
                    throw new RuntimeException("Malformed rule exception.");
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in parsing rules file.\n" + ex.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }

        return parsedRules.toArray(new Rule[0]);
    }
}
