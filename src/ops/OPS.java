package ops;

import config.LanguageManager;
import entity.Bet;
import entity.Die;
import entity.Match;
import entity.OpponentRepresentation;
import entity.Player;
import entity.PlayerBet;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

public class OPS implements Serializable {

    public static final String OPS_STREAM_NAME = "out";
    public static final String NORMAL_SITUATION_LABEL = "NORMAL";
    public static final String FINAL_BLOW_SITUATION_LABEL = "FINAL-BLOW";
    public static final String LAST_BET_SITUATION_LABEL = "LAST-BET";

    private final Rete ops;
    transient private StringWriter sw;

    public OPS() {
        this.ops = new Rete();
        this.sw = new StringWriter();
        ops.addOutputRouter(OPS_STREAM_NAME, sw);
    }

    public PlayerBet calculateBet(HashMap<Die, Integer> minValues) throws JessException {
        ops.executeCommand("(clear)");
        ops.executeCommand("(reset)");
        ops.executeCommand("(import ops.Core)");

        ops.store("minValues", new Value(minValues));
        ops.store("keyList", new Value(new ArrayList(minValues.keySet())));
        ops.store("len", new Value(minValues.keySet().size(), RU.INTEGER));
        ops.store("llama", new Value(Die.LLAMA));

        for (Rule r : RulesList.getRules()) {
            ops.executeCommand(r.getRule()
                    .replace("SALIENCE", r.getSalience())
                    .replace("NORMAL_SITUATION", NORMAL_SITUATION_LABEL)
                    .replace("FINAL_BLOW", FINAL_BLOW_SITUATION_LABEL)
                    .replace("LAST_BET", LAST_BET_SITUATION_LABEL)
                    .replace("STREAM_NAME", OPS_STREAM_NAME));
        }

        ops.run();

        String log = sw.toString();
        sw = new StringWriter();
        ops.addOutputRouter(OPS_STREAM_NAME, sw);

        return parseLog(log);
    }

    private PlayerBet parseLog(String log) {
        Die face = Die.TWO;
        int times = 0;

        String[] lines = log.split(System.getProperty("line.separator"));

        for (String l : lines) {
            if (l.startsWith("*SUGGESTED_BET: ")) {
                int startIndex = l.indexOf(" ") + 1;
                int endIndex = 0;
                if (l.charAt(startIndex) == 'D') {
                    times = 0;
                } else {
                    endIndex = l.indexOf(" ", startIndex + 1);
                    String timesString = l.substring(startIndex, endIndex);
                    times = Integer.parseInt(timesString);
                }

                if (times != 0) {
                    startIndex = l.lastIndexOf(" ") + 1;
                    endIndex = startIndex + 1;
                    String faceString = l.substring(startIndex, endIndex);
                    face = Die.getFromNumber(Integer.parseInt(faceString));
                }
            }
        }

        PlayerBet bet = new PlayerBet(new Bet(times, face), Core.getMatch().getCurrentPlayer().getID());
        bet.setExplanation(getReasoning(bet, log));
        return bet;
    }

    private String getReasoning(PlayerBet suggestedBet, String log) {
        StringBuilder reason = new StringBuilder();
        reason.append(LanguageManager.getValue("The expert says: "));
        reason.append(suggestedBet.getSimpleRepresentation()).append(LanguageManager.getValue(".\\n\\nThe reasoning performed was this:\\n"));
        Match m = Core.getMatch();
        for (Player p : m.getPlayers()) {
            if (p.getID() == m.getCurrentPlayer().getID() || p.getBets().isEmpty()) {
                continue;
            }

            OpponentRepresentation or = m.getCurrentPlayer().getOpponentRepresentation(p.getID());
            if (or.size() == 0) {
                continue;
            }

            reason.append(LanguageManager.getValue("In this turn XXX has bet:\\n").replace("XXX", p.getName()));
            for (Bet b : p.getBets()) {
                if (b instanceof PlayerBet) {
                    PlayerBet bet = (PlayerBet) b;
                    reason.append(bet.getSimpleRepresentation()).append("\n");
                } else {
                    reason.append(b.toString()).append("\n");
                }
            }

            reason.append(LanguageManager.getValue("The expert thinks that XXX may have these dice:\\n").replace("XXX", p.getName()));
            reason.append(or.toString()).append("\n\n");
        }

        String[] lines = log.split("\n");

        for (String l : lines) {
            if (l.startsWith("Started calculating")) {
                continue;
            }
            if (l.startsWith("Finished calculating")) {
                continue;
            }
            if (l.startsWith("Calculated bet")) {
                continue;
            }
            if (l.startsWith("*SUGGESTED_BET:")) {
                reason.append(l.replace("*SUGGESTED_BET:", LanguageManager.getValue("The bet with the highest desirability is: ")).replace(" times ", LanguageManager.getValue(" times ")).replace(" Die", ""));
                continue;
            }
            if (l.startsWith("Bet suggested.")) {
                continue;
            }
            if (l.startsWith("Game situation: ")) {
                String situation = l.substring(l.indexOf(":") + 2, l.length() - 1);
                reason.append(l.replace("Game situation: ", LanguageManager.getValue("Game situation: ")).replace(situation, LanguageManager.getValue(situation))).append("\n");
                continue;
            }
            if (l.startsWith("Dudo probability: ")) {
                reason.append(l.replace("Dudo probability: ", LanguageManager.getValue("Dudo probability: "))).append("\n");
                continue;
            }
            if (l.startsWith("Bluff threshold set to: ")) {
                reason.append(l.replace("Bluff threshold set to: ", LanguageManager.getValue("Bluff threshold set to: "))).append("\n");
                continue;
            }
            if (l.startsWith("Reliability threshold set to: ")) {
                reason.append(l.replace("Reliability threshold set to: ", LanguageManager.getValue("Reliability threshold set to: "))).append("\n");
                continue;
            }
            if (l.startsWith("Calculated unreliability of the bet at least ")) {
                reason.append(l.replace("Calculated unreliability of the bet at least ", LanguageManager.getValue("Calculated unreliability of the bet at least ")).replace(" times ", LanguageManager.getValue(" times "))).append("\n");
                continue;
            }
            if (l.startsWith("There is no need to calculate the probability of any bet. Dudo probability is 1.")) {
                reason.append(l.replace("There is no need to calculate the probability of any bet. Dudo probability is 1.", LanguageManager.getValue("There is no need to calculate the probability of any bet. Dudo probability is 1."))).append("\n");
                continue;
            }
            if (l.startsWith("You only have one die. Reliability threshold automatically increased to: ")) {
                reason.append(l.replace("You only have one die. Reliability threshold automatically increased to: ", LanguageManager.getValue("You only have one die. Reliability threshold automatically increased to: "))).append("\n");
                continue;
            }
            if (l.startsWith("A player is palifico, so there will ")) {
                reason.append(l.replace("A player is palifico, so there will be forced bet on not owned dice. It is better to doubt with higher probability. Dudo probability increased to: ", LanguageManager.getValue("A player is palifico, so there will be forced bet on not owned dice. It is better to doubt with higher probability. Dudo probability increased to: "))).append("\n");
                continue;
            }

            reason.append(l).append("\n");
        }

        return reason.toString();
    }
}
