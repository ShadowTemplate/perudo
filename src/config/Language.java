package config;

import java.util.Arrays;
import java.util.List;

public enum Language {

    ITALIAN,
    ENGLISH;

    @Override
    public String toString() {
        switch (this) {
            case ITALIAN:
                return "italiano";
            case ENGLISH:
                return "english";
            default:
                throw new AssertionError("Unknown language.");
        }
    }

    public static Language getLanguageFromString(String name) {
        switch (name) {
            case "italiano":
                return Language.ITALIAN;
            case "english":
                return Language.ENGLISH;
            default:
                throw new AssertionError("Unknown language.");
        }
    }

    public static List<String> getLanguagesList() {
        return Arrays.asList(Language.ITALIAN.toString(), Language.ENGLISH.toString());
    }

}
