package ops;

import config.CPULevel;
import entity.Bet;
import entity.Die;
import entity.Match;
import entity.OpponentRepresentation;
import entity.Player;
import entity.PlayerBet;
import gui.TipWindow;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import miscellanea.Constants;
import miscellanea.Handler;
import miscellanea.Utility;

public class Core {

    private static Match match = null;

    public static Match getMatch() {
        return match;
    }

    public static void setMatch(Match m) {
        match = m;
    }

    //States how much the current player will be proned to bluff
    public static double getBluffThreshold() {
        Player p = match.getCurrentPlayer();
        if (p.isPhysical()) {
            return TipWindow.getBluffValue();
        } else {
            double value = p.getBluffCoeff() - Handler.getNextPlayer(p, true).getTotalDudoNumber() / 10 - (3 - p.getDiceNumber()) / 10;
            if(match.getLevel() == CPULevel.EASY) {
                value = Math.max(Math.pow(value, 2), 0);
            } else {
                value = Math.max(Math.pow(value, 3), 0);
            }
            if(match.getRoundBetNumber() > 4) {
                value -= match.getRoundBetNumber()*0.05;
            }
            
            return Math.max(value, 0);
        }
    }

    //States how much the current player wants to feel safe with his bet
    public static double getReliabilityThreshold() {
        Player p = match.getCurrentPlayer();
        if (p.isPhysical()) {
            return 1 - TipWindow.getRiskValue();
        } else {
            switch (match.getLevel()) {
                case EASY:
                    return Constants.CPU_EASY_ACCEPTABILITY_TRESHOLD;
                case NORMAL:
                    return Constants.CPU_NORMAL_ACCEPTABILITY_TRESHOLD;
                case HARD:
                    return Constants.CPU_HARD_ACCEPTABILITY_TRESHOLD + Handler.getNextPlayer(p, true).getTotalDudoNumber() * 0.02;
                default:
                    return Constants.CPU_NORMAL_ACCEPTABILITY_TRESHOLD;
            }
        }
    }

    public static boolean isFirstBet() {
        return match.getBets().isEmpty();
    }

    public static boolean hasOneDie() {
        return match.getCurrentPlayer().getDiceNumber() == 1;
    }

    public static boolean isAnibodyPalifico() {
        return match.isAnybodyPalifico();
    }

    public static double fixReliabilityThreshold() {
        return Math.min(getReliabilityThreshold() + 0.2, 1);
    }

    public static double fixDudoProbability() {
        return Math.min(pDudo() + 0.15, 1);
    }

    //Calculates the upper bound for a range of bets to be calculated
    public static int getUpperBoundRangeLimitForBets(int lowerBound) {
        //The upper bound must not be greater than the total dice number in the match
        return Math.min(Math.max(lowerBound + 3, (int) (match.getTotalDiceNumber() / 2)), match.getTotalDiceNumber());
    }

    public static String getSituation() {
        if (match.getPlayersAliveNumber() > 2) {
            return OPS.NORMAL_SITUATION_LABEL;
        }

        Player p = match.getCurrentPlayer();
        Player opponent = Handler.getNextPlayer(p, true);

        if (opponent.getDiceNumber() == 1 && p.getDiceNumber() > 1) {
            return OPS.FINAL_BLOW_SITUATION_LABEL;
        }

        if (opponent.getDiceNumber() == 1 && p.getDiceNumber() == 1) {
            return OPS.LAST_BET_SITUATION_LABEL;
        }

        return OPS.NORMAL_SITUATION_LABEL;
    }

    //Calculates the probability of doubting the last bet
    public static double pDudo() {
        PlayerBet riskyBet = match.getLastBet();
        Player previousPlayer = match.getPlayers()[riskyBet.getPlayerID()];
        double k;

        if (match.isAnybodyPalifico()) {
            k = 1 - pAtLeastNTimesFaceForPlayer(null, riskyBet.getFace(), riskyBet.getTimes());
        } else {
            k = 1 - pAtLeastNTimesFaceOrLamaForPlayer(null, riskyBet.getFace(), riskyBet.getTimes());
        }

        if (match.getLevel() == CPULevel.EASY) {
            return k;
        }
        
        if(k == 0) {
            return k;
        }

        double z = k;
        if (previousPlayer.getBetMadeNumber() != 0) {
            z = fMeasure(k, previousPlayer.getPercentageWrongBets(), 0.4);
        }

        if (match.getLevel() == CPULevel.NORMAL) {
            return z;
        }

        if (previousPlayer.getBluffBetPercentage()!= 0) {
            z = fMeasure(z, previousPlayer.getBluffBetPercentage(), 0.15);
        }

        return z;
    }

    private static double pExactNTimesFaceOrLama(Die die, int times, int total) {
        int combinations = (factorial(total).divide(factorial(times).multiply(factorial(total - times)))).intValue();
        if (die != Die.LLAMA) {
            return Math.pow((double) 1 / 3, times) * Math.pow((double) 2 / 3, (total - times)) * combinations;
        } else {
            return Math.pow((double) 1 / 6, times) * Math.pow((double) 5 / 6, (total - times)) * combinations;
        }
    }

    private static double pExactNTimesFace(int times, int diceNum) {
        int combinations = (factorial(diceNum).divide(factorial(times).multiply(factorial(diceNum - times)))).intValue();
        return Math.pow((double) 1 / 6, times) * Math.pow((double) 5 / 6, (diceNum - times)) * combinations;
    }

    public static double pAtLeastNTimesFaceForPlayer(Player p, Die die, int times) {
        if (p == null) {
            p = match.getCurrentPlayer();
        }

        int goal = times;
        int unknownNum = match.getTotalDiceNumber() - p.getDiceNumber();
        for (Die d : p.getDice()) {
            if (d == die) {
                goal--;
            }
        }

        if (goal == 0) {
            return 1;
        }

        HashMap<Integer, OpponentRepresentation> depic = p.getOpponentsRepresentations();

        for (OpponentRepresentation rep : depic.values()) {
            int count = rep.count(die);
            goal -= count;
            unknownNum -= rep.size();
        }

        if (goal == 0) {
            return 1;
        }

        double acc = 0;
        for (int i = 0; i < goal; i++) {
            acc += pExactNTimesFace(i, unknownNum);
        }
        return 1 - acc;
    }

    public static double pAtLeastNTimesFaceOrLamaForPlayer(Player p, Die die, int times) {
        if (p == null) {
            p = match.getCurrentPlayer();
        }

        int goal = times;
        int unknownNum = match.getTotalDiceNumber() - p.getDiceNumber();
        for (Die d : p.getDice()) {
            if (d == die || d == Die.LLAMA) {
                goal--;
            }
        }

        if (goal == 0) {
            return 1;
        }

        HashMap<Integer, OpponentRepresentation> depic = p.getOpponentsRepresentations();

        for (OpponentRepresentation rep : depic.values()) {
            int count = rep.count(die);
            if (die != Die.LLAMA) {
                count += rep.count(Die.LLAMA);
            }
            goal -= count;
            unknownNum -= rep.size();
        }

        if (goal == 0) {
            return 1;
        }

        double acc = 0;
        for (int i = 0; i < goal; i++) {
            acc += pExactNTimesFaceOrLama(die, i, unknownNum);
        }
        return 1 - acc;
    }

    //Updates the bluff coefficient of a player according to the bets performed in the last turn
    public static void updateBluffCoefficient(Player p) {
        if (p.getBets().isEmpty() || (p.getBets().size() == 1 && p.getBets().getLast().getTimes() == 0)) {
            return;
        }

        double cb = p.getBluffCoeff(),
                k = estimateBluffPercentageFromPlayerBets(p) * 100,
                z = Constants.AVERAGE_BLUFF_PERCENTAGE * 100;

        double res = (k - z) / 200;
        res *= Math.abs(res);
        cb += res;

        if (estimateBluffForBet(p, p.getBets().getLast()) > Constants.AVERAGE_BLUFF_PERCENTAGE) { //If the first bet was a bluff
            cb += 0.08 + 0.01 * match.getTurnNumber();
        }

        if (cb >= 0.9) {
            cb = 0.9;
        }

        if (cb <= 0.1) {
            cb = 0.1;
        }

        p.setBluffCoeff(cb);
    }

    //Used by OPS
    public static double estimateBluffForBet(Player p, int times, Die d) {
        if (p == null) {
            p = match.getCurrentPlayer();
        }
        return estimateBluffForBet(p, new Bet(times, d));
    }

    //Estimates how much a bet is a bluff for the player
    public static double estimateBluffForBet(Player p, Bet bet) {
        if (p == null) {
            p = match.getCurrentPlayer();
        }

        ArrayList<Die> dice = p.getDice();
        Die face = bet.getFace();
        int times = bet.getTimes(),
                llamaCount = 0,
                targetFaceCount = 0;

        for (Die d : dice) {
            if (d == Die.LLAMA) {
                llamaCount++;
            }
            if (d == face) {
                targetFaceCount++;
            }
        }

        if (face != Die.LLAMA) {
            targetFaceCount += llamaCount;
        }

        if (targetFaceCount >= times) { //Player's dice make the bet already true
            return 0;
        }

        return 1 - (double) targetFaceCount / (double) times;
    }

    //Estimates how much a player has bluffed according to the bets performed in the last turn
    public static double estimateBluffPercentageFromPlayerBets(Player p) {
        ArrayDeque<Bet> bets = p.getBets();
        double t = 0;
        for (Bet b : bets) {
            if (b.getTimes() != 0) {
                t += estimateBluffForBet(p, b);
            }
        }
        return t / (double) bets.size();
    }

    //Updates the percentage of bluff bets performed by a player
    //It combines his past statistics (bigger weight) with the
    //the percentage in his last turn (lower weight)
    public static void updateBluffBetsPercentage(Player p) {
        if (p.getBets().isEmpty() || (p.getBets().size() == 1 && p.getBets().getLast().getTimes() == 0)) {
            return;
        }

        double bp = p.getBluffBetPercentage();
        double estimate = estimateBluffPercentageFromPlayerBets(p);

        if (bp == 0 && estimate == 0) {
            return;
        }

        if (bp == 0 && estimate != 0) {
            p.setBluffBetPercentage(estimate);
            return;
        }

        if (bp != 0 && estimate == 0) {
            p.setBluffBetPercentage(fMeasure(bp, 0.1, 0.3));
            return;
        }

        p.setBluffBetPercentage(fMeasure(bp, estimate, 0.3));

    }

    //According to the probability of a player bet, this method tries
    //to predict how many dice with the same face the player can have.
    //The riskier is the bet, the higher will be the number of dice quantified.
    public static int quantifyDice(int numDice, double betProbability) {
        int estimatedDiceNumber = 0;
        int limit = (int) (((double) 1 - betProbability) * 100) - 15;
        if (limit >= 85) {
            limit = 85;
        }
        if (limit <= 15) {
            limit = 15;
        }

        for (int i = 0; i < numDice; i++) {
            if (Utility.random.nextInt(100) <= limit) {
                estimatedDiceNumber++;
            }
        }
        return estimatedDiceNumber;
    }

    //Beta < 1 => v1 weight more
    public static double fMeasure(double v1, double v2, double beta) {
        return ((1 + beta * beta) * v1 * v2) / ((beta * beta * v1) + v2);
    }

    private static BigInteger recfact(long start, long n) {
        long i;
        if (n <= 16) {
            BigInteger r = new BigInteger(Long.toString(start));
            for (i = start + 1; i < start + n; i++) {
                r = r.multiply(new BigInteger(Long.toString(i)));
            }
            return r;
        }
        i = n / 2;
        return recfact(start, i).multiply(recfact(start + i, n - i));
    }

    private static BigInteger factorial(long n) {
        return recfact(1, n);
    }

    public static Bet getBetForLastBetSituation(HashMap<Die, Integer> minValues) {
        ArrayList<Die> dice = match.getCurrentPlayer().getDice();
        Die myFace = dice.get(0);
        if (match.getRoundBetNumber() == 1) {
            if (myFace == Die.LLAMA || myFace == Die.SIX) {
                return new Bet(1, Die.SIX);
            }

            if (match.getCurrentPlayer().getBluffCoeff() < 0.3) {
                return new Bet(1, myFace);
            }

            return new Bet(1, Die.getFromNumber(Utility.random.nextInt(5) + 2)); //One random die, except LLAMA
        }

        if (match.getRoundBetNumber() == 2) {
            if (myFace == Die.LLAMA || minValues.get(myFace) == 1) {
                return new Bet(1, myFace);
            }

            if (match.getLastBet().getFace() == myFace) {
                if (Utility.random.nextInt(2) > 0) { //50% - 50%
                    return new Bet(0, Die.TWO);
                } else {
                    return new Bet(2, myFace);
                }
            }

            return new Bet(0, Die.TWO);
        }

        if (minValues.get(myFace) == 1) {
            return new Bet(1, myFace);
        } else {
            return new Bet(0, Die.TWO);
        }

    }

    public static Bet getBetForFinalBlowSituation(HashMap<Die, Integer> minValues) {
        int[] faceCounter = {0, 0, 0, 0, 0, 0};
        for (Die d : match.getCurrentPlayer().getDice()) {
            faceCounter[d.ordinal()]++;
        }

        int maxValueIndex = faceCounter[1];
        Die maxFace = Die.TWO;
        for (int i = 2; i < faceCounter.length; i++) {
            if (faceCounter[i] >= faceCounter[maxValueIndex]) {
                maxValueIndex = i;
                maxFace = Die.getFromNumber(i + 1);
            }
        }

        int maxTimes = faceCounter[maxValueIndex];
        if (maxFace != Die.LLAMA) {
            maxTimes += faceCounter[0];
        }

        if (maxTimes >= minValues.get(maxFace)) {
            return new Bet(maxTimes, maxFace);
        }

        return new Bet(0, Die.TWO);
    }
}
