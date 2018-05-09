package entity;

import config.LanguageManager;
import java.io.Serializable;

public class Bet implements Serializable {

    private final int times; // TIMES = 0 -> Dudo
    private final Die face;

    public Bet(int times, Die face) {
        this.times = times;
        this.face = face;
    }

    public Bet(Bet b) {
        this.times = b.times;
        this.face = b.face;
    }

    public int getTimes() {
        return times;
    }

    public Die getFace() {
        return face;
    }

    @Override
    public String toString() {
        if (times == 0) {
            return "Dudo";
        }

        return times + LanguageManager.getValue(" times ") + face;
    }

}
