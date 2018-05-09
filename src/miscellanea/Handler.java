package miscellanea;

import config.CPULevel;
import config.LanguageManager;
import config.Settings;
import entity.Bet;
import entity.Die;
import entity.ListEntry;
import entity.Match;
import entity.OpponentRepresentation;
import entity.Player;
import entity.PlayerBet;
import gui.AskDiceWindow;
import gui.GameWindow;
import gui.SaveWindow;
import java.io.File;
import java.io.StringWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import jess.JessException;
import ops.Core;

public class Handler {

    private static GameWindow GUI;
    private static Match match = null;
    private static String message = "";

    public static void main(String[] args) {
        Utility.initGameFromFiles();
        GameWindow.initGUI();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        GUI = new GameWindow();
    }

    public static void initMatch() {
        Utility.shortenNames();
        Utility.removeNamesakes();

        if (match == null) { //First match of the session
            match = new Match();
        } else { //A new match after another one has just finished
            match = new Match(match.getPlayers());
        }

        Player[] players = match.getPlayers();
        String[] playersNames = new String[players.length];
        System.out.println("New match started. Players number: " + players.length);
        for (int i = 0; i < players.length; i++) {
            playersNames[i] = players[i].getName();
            System.out.println("Player " + players[i]);
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        GUI.cleanOldMatch(players[0].getDiceNumber());
        GUI.initLogNames(playersNames);
        GUI.setLogStatus(true);
        GUI.setInfoStatus(true);

        match.rollDice();
        Handler.printDice();
        prepareGUIForPlayer(match.getCurrentPlayer());
    }

    private static void updateInfoBox() {
        Player p = match.getCurrentPlayer();
        GUI.clearInfo();
        if (match.getLevel() != CPULevel.HARD) {
            GUI.addInfo(new ListEntry(LanguageManager.getValue("Turn"), "" + match.getTurnNumber()));
            GUI.addInfo(new ListEntry(LanguageManager.getValue("Bet made"), "" + match.getRoundBetNumber()));
        }

        GUI.addInfo(new ListEntry(LanguageManager.getValue("Players alive"), "" + match.getPlayersAliveNumber()));
        if (match.getLevel() == CPULevel.EASY) {
            GUI.addInfo(new ListEntry(LanguageManager.getValue("Total dice number"), "" + match.getTotalDiceNumber()));
            GUI.addInfo(new ListEntry(LanguageManager.getValue("XXX's dice").replace("XXX", p.getName()), "" + p.getDiceNumber()));
        }

        GUI.addInfo(new ListEntry(LanguageManager.getValue("Current player"), p.getName()));

        if (match.isAnybodyPalifico()) {
            GUI.addInfo(new ListEntry(LanguageManager.getValue("Palifico"), match.getPlayerPalifico().getName()));
        }
    }

    private static void prepareGUIForPlayer(Player currPlayer) {
        if (match.getRoundBetNumber() == 0) { //First bet of the turn
            checkPalifico();
        }

        updateInfoBox();
        HashMap<Die, Integer> possibleBets = getMinPossibleBetsPerFace(match.getLastBet(), match.isAnybodyPalifico(), currPlayer.getDiceNumber());

        if (match.getPlayers()[0].isAlive()) {
            GUI.setDice(match.getPlayers()[0].getDice()); //Shows user dice, even if he is not the current player
        }

        GUI.setAvatar(currPlayer.getAvatar());
        GUI.setOpponentName(currPlayer.isPhysical() ? "" : currPlayer.getName());

        if (currPlayer.isPhysical()) {
            GUI.setPlayerElementsStatus(true);
            GUI.setPlayerDiceStatus(true);
            GUI.setPossibleBets(true, possibleBets, match.getTotalDiceNumber());
            GUI.setOpponentNextBetStatus(false);
            GUI.setOpponentBoxStatus(false);

            return;
        }

        //Assert: currPlayer is !physical
        if (!currPlayer.isCPUControlled()) {
            GUI.setPlayerElementsStatus(false);
            GUI.setPlayerDiceStatus(false);
            GUI.setPossibleBets(false, possibleBets, match.getTotalDiceNumber());
            GUI.setOpponentNextBetStatus(true);
            GUI.setNextOpponentButtonLabel(LanguageManager.getValue("Insert bet"));
            GUI.setOpponentBoxStatus(true);

            return;
        }

        //Assert: currPlayer is CPUControlled
        GUI.setPlayerElementsStatus(false);
        GUI.setPlayerDiceStatus(false);
        GUI.setPossibleBets(false, possibleBets, match.getTotalDiceNumber());
        GUI.setOpponentNextBetStatus(true);
        GUI.setNextOpponentButtonLabel(LanguageManager.getValue("Calculate bet"));
        GUI.setOpponentBoxStatus(false);
    }

    public static void handleNewBet(Bet bet) {
        if (bet.getTimes() == 0 && match.getBets().isEmpty()) {
            JOptionPane.showMessageDialog(null, LanguageManager.getValue("Your first bet can not be a Dudo."), LanguageManager.getValue("ERROR"), JOptionPane.ERROR_MESSAGE);
        } else {
            Handler.executeBet(new PlayerBet(bet, match.getCurrentPlayer().getID()));
        }
    }

    public static void calculateBetForOpponent() {
        PlayerBet bet = null;
        try {
            bet = match.getOPS().calculateBet(getMinPossibleBetsPerFace(match.getLastBet(), match.isAnybodyPalifico(), match.getCurrentPlayer().getDiceNumber()));
        } catch (JessException ex) {
            System.err.println("Error in OPS at line " + ex.getLineNumber());
            System.err.println(ex.getMessage());
            System.err.println("Unable to calculate bet. Trivial bet will be used instead.");
            if (!match.getBets().isEmpty()) { //There is a bet: trivial raise
                PlayerBet b = match.getLastBet();
                bet = new PlayerBet(new Bet(b.getTimes() + 1, b.getFace()), match.getCurrentPlayer().getID());
            } else { //First bet of the turn: 1 time 2
                bet = new PlayerBet(new Bet(1, Die.TWO), match.getCurrentPlayer().getID());
            }
        } finally {
            executeBet(bet);
        }
    }

    public static void calculateBetForPlayer() {
        try {
            PlayerBet bet = match.getOPS().calculateBet(getMinPossibleBetsPerFace(match.getLastBet(), match.isAnybodyPalifico(), match.getCurrentPlayer().getDiceNumber()));
            GUI.showTip(bet);
        } catch (JessException ex) {
            JOptionPane.showMessageDialog(null, LanguageManager.getValue("We are unable to suggest you a bet: please do it on your own."), LanguageManager.getValue("ERROR"), JOptionPane.ERROR_MESSAGE);
            System.err.println("OPS error:\n" + ex.getMessage());
        }
    }

    private static boolean isExactBet(PlayerBet bet, boolean verbose) {
        message = "";
        int n = 0;
        int goal = bet.getTimes();
        Die face = bet.getFace();
        Player currPlayer = match.getCurrentPlayer();
        int firstID = currPlayer.getID();
        boolean continueCheck = true;
        boolean result = false;

        if (verbose) {
            System.out.println("Goal: " + bet.getSimpleRepresentation());
        }

        while (continueCheck) {
            if (verbose) {
                System.out.print(currPlayer.getName() + " has: ");
            }
            if (!currPlayer.isPhysical() && !currPlayer.isCPUControlled() && !currPlayer.hasShownDice()) { //It is an opponent with unknown dice
                Handler.requestDice(currPlayer);
            }

            currPlayer.setShownDice(true);

            for (Die d : currPlayer.getDice()) {
                if (match.isAnybodyPalifico()) {
                    if (d == face) {
                        n++;
                        if (verbose) {
                            System.out.print(d + " ");
                        }
                    }
                } else {
                    if (d == Die.LLAMA || d == face) {
                        n++;
                        if (verbose) {
                            System.out.print(d + " ");
                        }
                    }
                }

                if (n >= goal) {
                    continueCheck = false;
                    result = true;
                    break;
                }
            }

            currPlayer = getNextPlayer(currPlayer, true);
            if (currPlayer.getID() == firstID) {
                break;
            }
        }

        if (verbose) {
            System.out.println("Reached: " + n + " " + face);
            message = LanguageManager.getValue("Reached: ") + n + LanguageManager.getValue(" times ") + face + "\n";
        }

        return result;
    }

    private static void requestDice(Player forPlayer) {
        AskDiceWindow adw = new AskDiceWindow(forPlayer);
        adw.setBoxNumber();
        adw.setVisible(true);
    }

    private static void checkPalifico() {
        if (match.getCurrentPlayer().getDiceNumber() == 1 && match.getPlayersAliveNumber() > 2) {
            match.setPlayerPalifico(match.getCurrentPlayer());
        } else {
            match.setPlayerPalifico(null);
        }
    }

    public static Player getNextPlayer(Player pred, boolean mustBeAlive) {
        Player next;
        int currID = pred.getID();
        Player[] players = match.getPlayers();

        //Finds the next player
        while (true) {
            currID = (currID == players.length - 1) ? 0 : currID + 1;
            if (!mustBeAlive) {
                next = players[currID];
                break;
            } else {
                if (players[currID].isAlive()) {
                    next = players[currID];
                    break;
                }
            }
        }
        return next;
    }

    private static void setNextPlayerBeetweenAlive(Player pred) {
        Player p = getNextPlayer(pred, true);
        match.setCurrentPlayer(p.getID());
    }

    private static void updateRepresentations() {
        //Each player, with the exception of the current one, tries to understand which dice the current player has
        PlayerBet lastBet = match.getLastBet();
        System.out.println("Updating representations after " + lastBet.getSimpleRepresentation());

        Die face = lastBet.getFace();
        int times = lastBet.getTimes();
        Player currPlayer = match.getCurrentPlayer();
        int currID = currPlayer.getID();

        for (Player p : match.getPlayers()) {
            if (p.isAlive() && p.getID() != currID) {
                double prob;
                if (match.isAnybodyPalifico()) {
                    prob = Core.pAtLeastNTimesFaceForPlayer(p, face, times);
                } else {
                    prob = Core.pAtLeastNTimesFaceOrLamaForPlayer(p, face, times);
                }

                OpponentRepresentation or = p.getOpponentRepresentation(currID);
                int quant = Core.quantifyDice(times, prob);
                quant = Math.min(quant, p.getDiceNumber());
                System.out.println("Quantified: " + quant);

                if (match.getLevel() == CPULevel.HARD && quant > 0) {
                    int tries = (((int) (or.getEstimatedCB() * 100)) - 50) / 13;
                    for (int i = 0; i < tries; i++) {
                        if (Utility.random.nextInt(100) > 50 && quant > 0) {
                            quant--;
                            System.out.println("Due to opponent's CB, quantified lowered to: " + quant);
                        }
                    }
                }

                System.out.println("Previous representation of " + p.getName() + " for " + currPlayer.getName() + ": " + or);

                int freeSlot = currPlayer.getDiceNumber() - or.size();
                Math.max(freeSlot, 0);
                while (quant > 0) {
                    if (freeSlot > 0) {
                        if (Utility.random.nextInt(100) < 15) {
                            p.addPrediction(currID, Die.LLAMA);
                        } else {
                            p.addPrediction(currID, face);
                        }
                        freeSlot--;
                    } else {
                        int random = Utility.random.nextInt(100);
                        int limit = 20;
                        if (face == Die.LLAMA) {
                            limit = 85;
                        }
                        if (random < limit) {
                            //Changes a representation with a LLAMA
                            int index = Utility.random.nextInt(or.size());
                            or.remove(index);
                            or.addPrediction(Die.LLAMA);
                            freeSlot--;
                        }
                    }
                    quant--;
                }

                System.out.println("Post representation of " + p.getName() + " for " + currPlayer.getName() + ": " + or);
            }
        }
    }

    private static void executeBet(PlayerBet bet) {
        System.out.println("Executing bet: " + bet);
        Player currPlayer = match.getCurrentPlayer();

        if (match.getLevel() == CPULevel.HARD) {
            GUI.resetBetHistory();
        }

        GUI.addLogBet(currPlayer.getName(), bet.getSimpleRepresentation());

        PlayerBet previousBet = match.getLastBet(); //Needed in case of Dudo

        match.addBet(bet);
        match.increaseRoundBetNumber();
        match.increaseMatchBetNumber();

        currPlayer.addBet(bet);

        if (bet.getTimes() != 0) { //It is not a Dudo
            Handler.updateRepresentations();
            setNextPlayerBeetweenAlive(currPlayer);
            prepareGUIForPlayer(match.getCurrentPlayer()); //The current player has just been updated
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
            return;
        }

        //It is a Dudo
        Player betWinner;
        Player previousPlayer = match.getPlayers()[previousBet.getPlayerID()];
        currPlayer.increaseTotalDudoNumber();

        Player dieLoser;
        if (isExactBet(previousBet, true)) {
            dieLoser = currPlayer;
            betWinner = previousPlayer;
            JOptionPane.showMessageDialog(null, message + LanguageManager.getValue("XXX was a correct bet!\\nYYY has wrongly said Dudo.\\nYYY looses a die.").replace("XXX", previousBet.getSimpleRepresentation()).replace("YYY", dieLoser.getName()), LanguageManager.getValue("ATTENTION"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            dieLoser = previousPlayer;
            betWinner = currPlayer;
            JOptionPane.showMessageDialog(null, message + LanguageManager.getValue("XXX was a wrong bet!\\nYYY has correctly said Dudo.\\nZZZ looses a die.").replace("XXX", previousBet.getSimpleRepresentation()).replace("YYY", currPlayer.getName()).replace("ZZZ", dieLoser.getName()), LanguageManager.getValue("ATTENTION"), JOptionPane.INFORMATION_MESSAGE);
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        for (Player p : match.getPlayers()) {
            System.out.print(p.getName() + "'s BC: " + p.getBluffCoeff() + " -> ");
            Core.updateBluffCoefficient(p);
            System.out.println(p.getBluffCoeff());
            System.out.print(p.getName() + "'s B%: " + p.getBluffBetPercentage() + " -> ");
            Core.updateBluffBetsPercentage(p);
            System.out.println(p.getBluffBetPercentage());
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        if (match.getShownDicePlayerNumber() == match.getPlayersAliveNumber()) { //If every player has shown its dice
            for (PlayerBet b : match.getBets()) {
                Player author = match.getPlayers()[b.getPlayerID()];
                if (b.getTimes() != 0) { //Skip Dudo
                    if (isExactBet(b, false)) {
                        author.increaseExactBetMadeNumber();
                    }
                    author.increaseBetMadeNumber();

                    if (match.getLevel() == CPULevel.HARD) {
                        double bluff = Core.estimateBluffForBet(author, b);
                        for (Player currp : match.getPlayers()) {
                            if (currp.getID() != author.getID()) {
                                OpponentRepresentation or = currp.getOpponentRepresentation(author.getID());
                                double res = Core.fMeasure(or.getEstimatedCB(), Math.max(0.1, bluff), 0.45);
                                or.setEstimatedCB(res);
                            }
                        }
                    }
                }
            }
        }

        dieLoser.decreaseDiceNumber();

        match.setRoundBetNumber(0);
        match.increaseTurnNumber();

        if (match.getPlayersAliveNumber() == 1) {
            JOptionPane.showMessageDialog(null, LanguageManager.getValue("Match is over.\\nCongratulations to the winner: XXX.\\n\\nIn the next window you can choose which players to save for future matches.\\nSaved players will keep their game style.").replace("XXX", betWinner.getName()), LanguageManager.getValue("CONGRATULATIONS"), JOptionPane.INFORMATION_MESSAGE);
            Utility.updateSettingsFile(match.getPlayers()[0], Settings.language);
            SaveWindow sw = new SaveWindow();
            sw.addPlayers(match.getPlayers());
            sw.setVisible(true);
            JOptionPane.showMessageDialog(null, LanguageManager.getValue("A new match with the same players will now start!\\nYou can change the settings from Menu."), LanguageManager.getValue("NEW MATCH"), JOptionPane.INFORMATION_MESSAGE);
            initMatch();
            return;
        }

        match.setCurrentPlayer(dieLoser.getID());
        if (!dieLoser.isAlive()) {
            setNextPlayerBeetweenAlive(dieLoser);
        }

        match.getBets().clear();

        for (Player p : match.getPlayers()) {
            if (p.isAlive()) {
                p.getBets().clear();
                p.resetRepresentations();
            }
        }
        GUI.cleanOldMatch(match.getPlayers()[0].getDiceNumber());
        match.hideDice();
        match.rollDice();
        Handler.printDice();
        prepareGUIForPlayer(match.getCurrentPlayer());

    }

    public static HashMap<Die, Integer> getMinPossibleBetsPerFace(Bet lastBet, boolean palificoMode, int diceNumCurrPlayer) {
        HashMap<Die, Integer> minPossibleBetsPerFace = new HashMap<>();

        if (lastBet == null && !palificoMode) { //First bet of the turn, no Palifico
            //No LLAMA allowed
            minPossibleBetsPerFace.put(Die.TWO, 1);
            minPossibleBetsPerFace.put(Die.THREE, 1);
            minPossibleBetsPerFace.put(Die.FOUR, 1);
            minPossibleBetsPerFace.put(Die.FIVE, 1);
            minPossibleBetsPerFace.put(Die.SIX, 1);
            return minPossibleBetsPerFace;
        }

        if (lastBet == null && palificoMode) { //First bet of the turn, Palifico
            minPossibleBetsPerFace.put(Die.LLAMA, 1);
            minPossibleBetsPerFace.put(Die.TWO, 1);
            minPossibleBetsPerFace.put(Die.THREE, 1);
            minPossibleBetsPerFace.put(Die.FOUR, 1);
            minPossibleBetsPerFace.put(Die.FIVE, 1);
            minPossibleBetsPerFace.put(Die.SIX, 1);
            return minPossibleBetsPerFace;
        }

        int times = lastBet.getTimes();
        Die face = lastBet.getFace();

        if (!palificoMode) { //No first bet of the turn, no one Palifico
            if (face == Die.LLAMA) { //The previous bet was on LLAMA
                minPossibleBetsPerFace.put(Die.LLAMA, times + 1);
                minPossibleBetsPerFace.put(Die.TWO, 2 * times + 1);
                minPossibleBetsPerFace.put(Die.THREE, 2 * times + 1);
                minPossibleBetsPerFace.put(Die.FOUR, 2 * times + 1);
                minPossibleBetsPerFace.put(Die.FIVE, 2 * times + 1);
                minPossibleBetsPerFace.put(Die.SIX, 2 * times + 1);
                return minPossibleBetsPerFace;
            } else { //The previous bet was not on LLAMA
                minPossibleBetsPerFace.put(Die.LLAMA, (int) Math.ceil((double) times / 2));
                minPossibleBetsPerFace.put(Die.TWO, times + 1);
                minPossibleBetsPerFace.put(Die.THREE, Die.bigger(Die.THREE, face) ? times : times + 1);
                minPossibleBetsPerFace.put(Die.FOUR, Die.bigger(Die.FOUR, face) ? times : times + 1);
                minPossibleBetsPerFace.put(Die.FIVE, Die.bigger(Die.FIVE, face) ? times : times + 1);
                minPossibleBetsPerFace.put(Die.SIX, Die.bigger(Die.SIX, face) ? times : times + 1);
                return minPossibleBetsPerFace;
            }
        } else { //No first bet of the turn, someone Palifico
            if (diceNumCurrPlayer > 1) { //I have at least 2 dice: I can not change the face
                minPossibleBetsPerFace.put(face, times + 1);
                return minPossibleBetsPerFace;
            } else { //I only have one die: I can change the face
                minPossibleBetsPerFace.put(Die.LLAMA, times + 1);
                minPossibleBetsPerFace.put(Die.TWO, Die.bigger(Die.TWO, face) ? times : times + 1);
                minPossibleBetsPerFace.put(Die.THREE, Die.bigger(Die.THREE, face) ? times : times + 1);
                minPossibleBetsPerFace.put(Die.FOUR, Die.bigger(Die.FOUR, face) ? times : times + 1);
                minPossibleBetsPerFace.put(Die.FIVE, Die.bigger(Die.FIVE, face) ? times : times + 1);
                minPossibleBetsPerFace.put(Die.SIX, Die.bigger(Die.SIX, face) ? times : times + 1);
                return minPossibleBetsPerFace;
            }
        }
    }

    public static void resetMatch() {
        match = null;
    }

    public static void savePlayers(ArrayList<String> names) {
        StringWriter log = new StringWriter();
        for (String name : names) {
            Player currPlayer = null;
            for (Player p : match.getPlayers()) {
                if (p.getName().equals(name)) {
                    currPlayer = p;
                    break;
                }
            }
            String fileName = Constants.PERUDO_HOME_PATH + File.separator + "player_" + currPlayer.getName() + Constants.PLAYER_FILE_EXTENSION;
            boolean res = Utility.savePlayerToFile(fileName, currPlayer);
            if (res) {
                log.append(LanguageManager.getValue("Saved: ") + fileName + "\n");
            } else {
                log.append(LanguageManager.getValue("Error in saving: ") + fileName + "\n");
            }
        }

        if (!log.toString().equals("")) {
            JOptionPane.showMessageDialog(null, log, LanguageManager.getValue("RESULT"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void saveMatch() {
        if (match == null) {
            JOptionPane.showMessageDialog(null, LanguageManager.getValue("No match to save."), LanguageManager.getValue("ERROR"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringWriter log = new StringWriter();
        String filename = Constants.PERUDO_HOME_PATH + File.separator + "match_" + Utility.getTimeStamp() + Constants.MATCH_FILE_EXTENSION;
        boolean res = Utility.saveMatchToFile(filename, match);

        if (res) {
            log.append(LanguageManager.getValue("Saved: ") + filename + "\n");
        } else {
            log.append(LanguageManager.getValue("Error in saving: ") + filename + "\n");
        }

        if (!log.toString().equals("")) {
            JOptionPane.showMessageDialog(null, log, LanguageManager.getValue("RESULT"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void loadMatch(String filename) {
        Match newMatch = Utility.loadMatchFromFile(filename);

        if (newMatch == null) {
            JOptionPane.showMessageDialog(null, LanguageManager.getValue("Error in loading: ") + filename, LanguageManager.getValue("ERROR"), JOptionPane.ERROR_MESSAGE);
            return;
        }

        match = newMatch;
        Player[] players = match.getPlayers();
        String[] playersNames = new String[players.length];
        System.out.println("Loaded match. Players number: " + players.length);
        for (int i = 0; i < players.length; i++) {
            playersNames[i] = players[i].getName();
            System.out.println("Player " + players[i]);
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        GUI.cleanOldMatch(players[0].getDiceNumber());
        GUI.initLogNames(playersNames);
        GUI.setLogStatus(true);
        GUI.setInfoStatus(true);

        Handler.printDice();
        prepareGUIForPlayer(match.getCurrentPlayer());
        match.restartOPS();
        Core.setMatch(match);
        restoreLog();
    }

    private static void restoreLog() {
        ArrayDeque<PlayerBet> bets = new ArrayDeque<>();
        if (bets.isEmpty()) {
            return;
        }

        for (PlayerBet b : match.getBets()) {
            bets.add(b);
        }

        if (match.getLevel() != CPULevel.HARD) {
            int size = bets.size();
            while (size > 1) {
                PlayerBet oldestBet = bets.getLast();
                GUI.addLogBet(match.getPlayers()[oldestBet.getPlayerID()].getName(), oldestBet.getSimpleRepresentation());
                bets.remove(oldestBet);
                size--;
            }
        }

        PlayerBet lastBet = match.getLastBet();
        GUI.addLogBet(match.getPlayers()[lastBet.getPlayerID()].getName(), lastBet.getSimpleRepresentation());
        bets.remove(lastBet);
    }

    private static void printDice() {
        for (Player p : match.getPlayers()) {
            if (p.isAlive()) {
                System.out.print(p.getName() + "'s dice: ");
                for (Die d : p.getDice()) {
                    System.out.print(d + " ");
                }
                System.out.println("");
            }
        }
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
    }
}
