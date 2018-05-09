package entity;

import config.LanguageManager;
import java.io.Serializable;

public enum Die implements Serializable {

    LLAMA,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX;

    public static boolean bigger(Die a, Die b) {
        return a.ord() > b.ord();
    }

    private int ord() {
        switch (this) {
            case LLAMA:
                return 1;
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            default:
                return 6;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case LLAMA:
                return LanguageManager.getValue("Llama Head");
            default:
                return this.ord() + "";
        }
    }

    public String toOPSString() {
        return "" + this.ord();
    }

    public static Die getFromNumber(int n) {
        switch (n) {
            case 1:
                return Die.LLAMA;
            case 2:
                return Die.TWO;
            case 3:
                return Die.THREE;
            case 4:
                return Die.FOUR;
            case 5:
                return Die.FIVE;
            default:
                return Die.SIX;
        }
    }

}
