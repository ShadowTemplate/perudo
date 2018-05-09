package miscellanea;

import config.CPULevel;
import config.Language;

public class Constants {

    public static final String RULES_URL = "https://www.dropbox.com/s/9q4zcca6ftqd6sd/Regolamento%20Perudo.pdf";

    public static final String SEPARATOR = System.getProperty("file.separator");
    public static final String PERUDO_HOME_PATH = System.getProperty("user.home") + SEPARATOR + "Perudo";

    public static final String LANGUAGE_PLACEHOLDER = "XXXXX";
    public static final String LANGUAGE_FILE_PATH = PERUDO_HOME_PATH + SEPARATOR + "perudo_lang_" + LANGUAGE_PLACEHOLDER + ".txt";

    public static final String GENDER_PLACEHOLDER = "YYYYY";
    public static final String GENDER_MALE = "male";
    public static final String GENDER_FEMALE = "female";
    public static final String AVATAR_EXTENSION = ".png";
    public static final String NAMES_FILE_PATH = PERUDO_HOME_PATH + SEPARATOR + "perudo_names_" + GENDER_PLACEHOLDER + "_" + LANGUAGE_PLACEHOLDER + ".txt";
    public static final String AVATAR_FOLDER = PERUDO_HOME_PATH + SEPARATOR + "perudo_avatars_" + GENDER_PLACEHOLDER;
    public static final String DIE_FOLDER = PERUDO_HOME_PATH + SEPARATOR + "perudo_dice" + SEPARATOR + "NUM.png";

    public static final String CONFIG_FILE_PATH = PERUDO_HOME_PATH + SEPARATOR + "perudo_config.bin";
    public static final String KEY_VALUE_SEPARATOR = "=";
    public static final String PLAYER_NAME_LABEL = "player_name";
    public static final String USER_LANGUAGE_LABEL = "language";
    public static final String BLUFF_COEFFICIENT_LABEL = "cb";
    public static final String BET_MADE_NUMBER_LABEL = "totbet";
    public static final String EXACT_BET_MADE_NUMBER_LABEL = "exactbet";
    public static final String BLUFF_BET_PERCENTAGE_LABEL = "bluffbet%";

    public static final String ENVIRONMENT_RULES_FILE_PATH = PERUDO_HOME_PATH + SEPARATOR + "perudo_rules.bin";
    public static final String ENVIRONMENT_RULES_SEPERATOR = "~";

    public static final Language DEFAULT_LANGUAGE = Language.ITALIAN;
    public static final String PLAYER_FILE_EXTENSION = ".ply";
    public static final String MATCH_FILE_EXTENSION = ".mch";
    public static final int MAX_PLAYER_NAME_LENGTH = 12;

    public static final String DEFAULT_PLAYER_NAME = "Alan";
    public static final int DEFAULT_PLAYERS_NUMBER = 4;
    public static final int MAXIMUM_PLAYERS_NUMBER = 8;
    public static final int INITIAL_DICE_NUMBER = 5;
    public static final CPULevel DEFAULT_CPU_LEVEL = CPULevel.EASY;
    public static final double DEFAULT_BLUFF_COEFF = 0.5;

    public static final double CPU_EASY_ACCEPTABILITY_TRESHOLD = 0.65;
    public static final double CPU_NORMAL_ACCEPTABILITY_TRESHOLD = 0.75;
    public static final double CPU_HARD_ACCEPTABILITY_TRESHOLD = 0.85;
    public static final double AVERAGE_BLUFF_PERCENTAGE = 0.75;

}
