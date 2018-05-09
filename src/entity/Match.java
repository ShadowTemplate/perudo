package entity;

import config.CPULevel;
import config.Settings;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import miscellanea.Constants;
import miscellanea.Utility;
import ops.Core;
import ops.OPS;

public class Match implements Serializable {

    //Each turn lasts a certain number of round
    private final Player[] players;
    private int currentPlayerIndex;
    private int turnNumber;
    private int totalRoundBetsNumber;
    private int totalMatchBetsNumber;
    private Player playerPalifico;
    private final ArrayDeque<PlayerBet> bets;
    private OPS env;
    private CPULevel level;

    public Match() {
        int playersNum = Settings.playersNum;
        players = new Player[playersNum];
        players[0] = new Player(Settings.playersTO.get(0), 0, Constants.INITIAL_DICE_NUMBER, playersNum);
        for (int i = 1; i < playersNum; i++) {
            players[i] = new Player(Settings.playersTO.get(i), i, Constants.INITIAL_DICE_NUMBER, playersNum);
        }
        currentPlayerIndex = Utility.random.nextInt(playersNum);
        turnNumber = 1;
        totalRoundBetsNumber = 0;
        totalMatchBetsNumber = 0;
        playerPalifico = null;
        bets = new ArrayDeque<>();
        this.env = new OPS();
        Core.setMatch(this);
        this.level = Settings.CPULevel;
    }

    public Match(Player[] pl) {
        players = pl;
        for (Player p : players) {
            p.setDiceNumber(Constants.INITIAL_DICE_NUMBER);
            p.setTotalDudoNumber(0);
            p.getDice().clear();
            p.getBets().clear();
            p.resetRepresentations();
            p.setShownDice(false);
        }

        currentPlayerIndex = Utility.random.nextInt(players.length);
        turnNumber = 1;
        totalRoundBetsNumber = 0;
        totalMatchBetsNumber = 0;
        playerPalifico = null;
        bets = new ArrayDeque<>();
        this.env = new OPS();
        Core.setMatch(this);
        this.level = Settings.CPULevel;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayerIndex = currentPlayer;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void increaseTurnNumber() {
        this.turnNumber += 1;
    }

    public int getTotalDiceNumber() {
        int n = 0;
        for (Player p : players) {
            n += p.getDiceNumber();
        }

        return n;
    }

    public OPS getEnv() {
        return env;
    }

    public int getPlayersAliveNumber() {
        int n = 0;
        for (Player p : players) {
            n += p.isAlive() ? 1 : 0;
        }

        return n;
    }

    public int getRoundBetNumber() {
        return totalRoundBetsNumber;
    }

    public void increaseRoundBetNumber() {
        this.totalRoundBetsNumber += 1;
    }

    public void setRoundBetNumber(int val) {
        this.totalRoundBetsNumber = val;
    }

    public int getMatchBetNumber() {
        return totalMatchBetsNumber;
    }

    public void increaseMatchBetNumber() {
        this.totalMatchBetsNumber += 1;
    }

    public Player getPlayerPalifico() {
        return playerPalifico;
    }

    public void setPlayerPalifico(Player p) {
        this.playerPalifico = p;
    }

    public boolean isAnybodyPalifico() {
        return playerPalifico != null;
    }

    public ArrayDeque<PlayerBet> getBets() {
        return bets;
    }

    public void addBet(PlayerBet b) {
        bets.addFirst(b);
    }

    public PlayerBet getLastBet() {
        return bets.size() > 0 ? bets.getFirst() : null;
    }

    public OPS getOPS() {
        return env;
    }

    public int getShownDicePlayerNumber() {
        int count = 0;
        for (Player p : players) {
            if (p.isAlive() && p.hasShownDice()) {
                count++;
            }
        }
        return count;
    }

    public void hideDice() {
        for (Player p : players) {
            p.setShownDice(false);
        }
    }

    public void rollDice() {
        for (Player p : players) {
            if (p.isAlive() && (p.isPhysical() || p.isCPUControlled())) {
                p.rollDice();
            } else {
                p.setDice(new ArrayList<Die>());
            }
        }
    }

    public void restartOPS() {
        this.env = new OPS();
    }

    public CPULevel getLevel() {
        return level;
    }

    public void setLevel(CPULevel level) {
        this.level = level;
    }
}
