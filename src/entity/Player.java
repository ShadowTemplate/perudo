package entity;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import miscellanea.Utility;

public class Player extends PlayerTO implements Serializable {

    private final int ID;

    private int activeDiceNumber;
    private int totalDudoNumber;
    private final ArrayList<Die> dice;
    private final ArrayDeque<Bet> bets;
    private final HashMap<Integer, OpponentRepresentation> opponentsRepresentations;

    private boolean hasShownDice;

    public Player(PlayerTO playerTO, int ID, int diceNumber, int playersNumber) {
        super(playerTO);
        this.ID = ID;

        this.activeDiceNumber = diceNumber;
        this.dice = new ArrayList<>();
        this.bets = new ArrayDeque<>();
        this.opponentsRepresentations = new HashMap<>();
        for (int i = 0; i < playersNumber; i++) {
            if (i != ID) {
                opponentsRepresentations.put(i, new OpponentRepresentation());
            }
        }
        hasShownDice = false;
    }

    public int getID() {
        return ID;
    }

    public boolean isAlive() {
        return activeDiceNumber > 0;
    }

    public int getDiceNumber() {
        return activeDiceNumber;
    }

    public void setDiceNumber(int num) {
        this.activeDiceNumber = num;
    }

    public void decreaseDiceNumber() {
        this.activeDiceNumber -= 1;
    }

    public int getTotalDudoNumber() {
        return totalDudoNumber;
    }

    public void setTotalDudoNumber(int num) {
        this.totalDudoNumber = num;
    }

    public void increaseTotalDudoNumber() {
        this.totalDudoNumber += 1;
    }

    public ArrayList<Die> getDice() {
        return dice;
    }

    public void rollDice() {
        this.dice.clear();
        for (int i = 0; i < activeDiceNumber; i++) {
            this.dice.add(Die.getFromNumber(Utility.random.nextInt(7)));
        }
    }

    public void setDice(ArrayList<Die> dice) {
        this.dice.clear();
        this.dice.addAll(dice);
    }

    public ArrayDeque<Bet> getBets() {
        return bets;
    }

    public void addBet(Bet bet) {
        this.bets.add(bet);
    }

    public int getNumberOfBets() {
        return this.bets.size();
    }

    public void addPrediction(int opponentID, Die prediction) {
        this.getOpponentsRepresentations().get(opponentID).addPrediction(prediction);
    }

    public void resetRepresentations() {
        for (OpponentRepresentation rep : opponentsRepresentations.values()) {
            rep.resetPrediction();
        }
    }

    public HashMap<Integer, OpponentRepresentation> getOpponentsRepresentations() {
        return opponentsRepresentations;
    }

    public OpponentRepresentation getOpponentRepresentation(Integer key) {
        return opponentsRepresentations.get(key);
    }

    public boolean hasShownDice() {
        return hasShownDice;
    }

    public void setShownDice(boolean flag) {
        hasShownDice = flag;
    }

    @Override
    public String toString() {
        return "" + ID + ": [TO: " + super.toString() + "]";
    }
}
