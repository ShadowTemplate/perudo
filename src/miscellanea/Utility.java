package miscellanea;

import config.CPULevel;
import config.Language;
import config.LanguageManager;
import config.Settings;
import entity.Match;
import entity.Player;
import entity.PlayerTO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Random;
import java.util.TreeSet;
import ops.RulesList;

public class Utility {

    public static final Random random = new Random();

    public static void initGameFromFiles() {
        if (!existsFile(Constants.PERUDO_HOME_PATH)) {
            if (!((new File(Constants.PERUDO_HOME_PATH)).mkdirs())) {
                throw new RuntimeException("Unable to create prefences folder.");
            }
        } else {
            System.out.println("Game folder found at " + Constants.PERUDO_HOME_PATH + "\n");
        }

        if (existsFile(Constants.CONFIG_FILE_PATH)) {
            System.out.println("Preferences loaded from file " + Constants.CONFIG_FILE_PATH);
            String lang = getValueFromFile(Constants.CONFIG_FILE_PATH, Constants.USER_LANGUAGE_LABEL);
            if (lang != null) {
                Settings.language = Language.getLanguageFromString(lang);
            }

            String playerName = getValueFromFile(Constants.CONFIG_FILE_PATH, Constants.PLAYER_NAME_LABEL);
            if (playerName != null) {
                Settings.playersTO.get(0).setName(playerName);
            }

            String bluffCoeff = getValueFromFile(Constants.CONFIG_FILE_PATH, Constants.BLUFF_COEFFICIENT_LABEL);
            if (bluffCoeff != null) {
                Settings.playersTO.get(0).setBluffCoeff(Double.parseDouble(bluffCoeff));
            }

            String betMadeNumber = getValueFromFile(Constants.CONFIG_FILE_PATH, Constants.BET_MADE_NUMBER_LABEL);
            if (betMadeNumber != null) {
                Settings.playersTO.get(0).setBetMadeNumber(Integer.parseInt(betMadeNumber));
            }

            String exactBetMadeNumber = getValueFromFile(Constants.CONFIG_FILE_PATH, Constants.EXACT_BET_MADE_NUMBER_LABEL);
            if (exactBetMadeNumber != null) {
                Settings.playersTO.get(0).setExactBetMadeNumber(Integer.parseInt(exactBetMadeNumber));
            }

            String bluffPerc = getValueFromFile(Constants.CONFIG_FILE_PATH, Constants.BLUFF_BET_PERCENTAGE_LABEL);
            if (exactBetMadeNumber != null) {
                Settings.playersTO.get(0).setBluffBetPercentage(Double.parseDouble(bluffPerc));
            }
        } else {
            System.err.println("Unable to find preferences. Default settings will be created and used.");
            if (!createDefaultSettingsFile()) {
                throw new RuntimeException("Unable to create default settings file.");
            }
        }

        updateCPULevel();

        System.out.println("Player stats: " + Settings.playersTO.get(0));
        System.out.println("Language: " + Settings.language.toString());
        System.out.println("Difficulty: " + Settings.CPULevel + "\n");

        NameGenerator.loadNames();
        LanguageManager.loadTranslations();

        if (!existsFile(Constants.ENVIRONMENT_RULES_FILE_PATH)) {
            System.err.println("Unable to find rules file. Default rule files will be created for future matches.");
            if (!createDefaultRulesFile()) {
                throw new RuntimeException("Unable to create default rules file.");
            }
        } else {
            RulesList.setRules(RulesList.parseRules());
        }
    }

    public static String getValueFromFile(String filename, String key) {
        BufferedReader in = null;
        String value = null;
        try {
            in = new BufferedReader(new FileReader(filename));
            String buffer;
            String[] splits;
            while ((buffer = in.readLine()) != null) {
                splits = buffer.split(Constants.KEY_VALUE_SEPARATOR);
                if (splits[0].equals(key)) {
                    value = splits[1];
                    break;
                }
            }
        } catch (IOException ex) {
            value = null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
        return value;
    }

    public static boolean existsFile(String filename) {
        File f = new File(filename);
        return f.exists();
    }

    private static boolean createDefaultSettingsFile() {
        boolean success = false;
        PrintWriter out = null;
        try {
            File f = new File(Constants.CONFIG_FILE_PATH);
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)), true);
            out.println(Constants.PLAYER_NAME_LABEL + Constants.KEY_VALUE_SEPARATOR + Constants.DEFAULT_PLAYER_NAME);
            out.println(Constants.USER_LANGUAGE_LABEL + Constants.KEY_VALUE_SEPARATOR + Constants.DEFAULT_LANGUAGE);
            out.println(Constants.BLUFF_COEFFICIENT_LABEL + Constants.KEY_VALUE_SEPARATOR + Constants.DEFAULT_BLUFF_COEFF);
            out.println(Constants.BET_MADE_NUMBER_LABEL + Constants.KEY_VALUE_SEPARATOR + 0);
            out.println(Constants.EXACT_BET_MADE_NUMBER_LABEL + Constants.KEY_VALUE_SEPARATOR + 0);
            out.println(Constants.BLUFF_BET_PERCENTAGE_LABEL + Constants.KEY_VALUE_SEPARATOR + 0);
            success = true;
        } catch (FileNotFoundException ex) {
            success = false;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return success;
    }

    private static boolean createDefaultRulesFile() {
        boolean success = false;
        PrintWriter out = null;
        try {
            File f = new File(Constants.ENVIRONMENT_RULES_FILE_PATH);
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)), true);
            String[] rules = RulesList.getEncodedRules();
            for (String rule : rules) {
                out.println(rule);
            }
            success = true;
        } catch (FileNotFoundException ex) {
            success = false;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return success;
    }

    public static PlayerTO loadPlayerFromFile(String filename) {
        ObjectInputStream ois = null;
        PlayerTO p;
        try {
            ois = new ObjectInputStream(new FileInputStream(filename));
            p = (PlayerTO) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            p = null;
            System.err.println(ex.getMessage());
        } finally {
            try {
                ois.close();
            } catch (IOException | NullPointerException ex) {
                System.err.println(ex.getMessage());
            }
        }

        return p;
    }

    public static boolean savePlayerToFile(String filename, Player p) {
        PlayerTO player = new PlayerTO(p.getName(), p.isMan(), p.getAvatar(), p.isPhysical(), p.isCPUControlled(),
                p.getBluffCoeff(), p.getBetMadeNumber(), p.getExactBetMadeNumber(), p.getBluffBetPercentage());

        boolean result;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(player);
            oos.close();
            result = true;
        } catch (IOException e) {
            result = false;
            System.err.println(e.getMessage());
        }
        return result;
    }

    public static Match loadMatchFromFile(String filename) {
        ObjectInputStream ois = null;
        Match m;
        try {
            ois = new ObjectInputStream(new FileInputStream(filename));
            m = (Match) ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            m = null;
            System.err.println(ex.getMessage());
        } finally {
            try {
                ois.close();
            } catch (IOException | NullPointerException ex) {
                System.err.println(ex.getMessage());
            }
        }

        return m;
    }

    public static boolean saveMatchToFile(String filename, Match m) {
        boolean result;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(m);
            oos.close();
            result = true;
        } catch (IOException e) {
            result = false;
            System.err.println(e.getMessage());
        }
        return result;
    }

    public static String getTimeStamp() {
        java.util.Date date = new java.util.Date();
        String timestamp = new Timestamp(date.getTime()).toString();
        timestamp = timestamp.replace(":", ".");
        return timestamp;
    }

    public static void shortenNames() {
        for (PlayerTO p : Settings.playersTO) {
            if (p.getName().length() > Constants.MAX_PLAYER_NAME_LENGTH) {
                p.setName(p.getName().substring(0, Constants.MAX_PLAYER_NAME_LENGTH));
            }
        }
    }

    public static void removeNamesakes() {
        TreeSet<String> names = new TreeSet<>();
        for (PlayerTO p : Settings.playersTO) {
            String currName = p.getName();
            String newName = currName;
            if (names.contains(currName)) {
                for (int j = 0; j < Constants.MAXIMUM_PLAYERS_NUMBER + 1; j++) {
                    newName = currName + j;
                    if (!names.contains(newName)) {
                        p.setName(newName);
                        break;
                    }
                }
            }
            names.add(newName);
        }
    }

    private static void updateCPULevel() {
        int betMade = Settings.playersTO.get(0).getBetMadeNumber(),
                exactBetMade = Settings.playersTO.get(0).getExactBetMadeNumber();

        if (betMade == 0) {
            return;
        }

        double strength = (double) exactBetMade / (double) betMade;
        if (strength > 0.65) {
            Settings.CPULevel = CPULevel.NORMAL;
        }

        if (strength > 0.8) {
            Settings.CPULevel = CPULevel.HARD;
        }
    }

    public static boolean updateSettingsFile(PlayerTO p, Language language) {
        boolean success = false;
        PrintWriter out = null;
        try {
            File f = new File(Constants.CONFIG_FILE_PATH);
            f.delete();
            out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f)), true);
            out.println(Constants.PLAYER_NAME_LABEL + Constants.KEY_VALUE_SEPARATOR + p.getName());
            out.println(Constants.USER_LANGUAGE_LABEL + Constants.KEY_VALUE_SEPARATOR + language);
            out.println(Constants.BLUFF_COEFFICIENT_LABEL + Constants.KEY_VALUE_SEPARATOR + p.getBluffCoeff());
            out.println(Constants.BET_MADE_NUMBER_LABEL + Constants.KEY_VALUE_SEPARATOR + p.getBetMadeNumber());
            out.println(Constants.EXACT_BET_MADE_NUMBER_LABEL + Constants.KEY_VALUE_SEPARATOR + p.getExactBetMadeNumber());
            out.println(Constants.BLUFF_BET_PERCENTAGE_LABEL + Constants.KEY_VALUE_SEPARATOR + p.getBluffBetPercentage());
            success = true;
        } catch (FileNotFoundException ex) {
            success = false;
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return success;
    }
}
