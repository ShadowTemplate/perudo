package config;

import java.util.Arrays;
import java.util.List;

public enum CPULevel {

    EASY,
    NORMAL,
    HARD;

    @Override
    public String toString() {
        switch (this) {
            case EASY:
                return LanguageManager.getValue("easy");
            case NORMAL:
                return LanguageManager.getValue("normal");
            case HARD:
                return LanguageManager.getValue("hard");
            default:
                throw new AssertionError("Unknown CPU Level.");
        }
    }

    public static CPULevel getLanguageFromString(String name) {
        if (name.equals(LanguageManager.getValue("easy"))) {
            return CPULevel.EASY;
        }

        if (name.equals(LanguageManager.getValue("normal"))) {
            return CPULevel.NORMAL;
        }

        if (name.equals(LanguageManager.getValue("hard"))) {
            return CPULevel.HARD;
        }

        throw new AssertionError("Unknown CPU Level.");

    }

    public static List<String> getCPUList() {
        return Arrays.asList(CPULevel.EASY.toString(), CPULevel.NORMAL.toString(), CPULevel.HARD.toString());
    }

}
