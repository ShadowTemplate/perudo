package entity;

import config.LanguageManager;
import java.io.Serializable;

public class PlayerBet extends Bet implements Serializable {

    private final int playerID;
    private String explanation = new String();

    public PlayerBet(Bet b, int ID) {
        super(b);
        this.playerID = ID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return "[" + playerID + "]: " + (super.getTimes() == 0 ? "Dudo" : "" + super.getTimes() + LanguageManager.getValue(" times ") + super.getFace());

    }

    public String getSimpleRepresentation() {
        return super.toString();
    }
}
