package config;

import miscellanea.NameGenerator;
import miscellanea.AvatarGenerator;
import entity.PlayerTO;
import java.util.ArrayList;
import miscellanea.Constants;

public class Settings {

    public static CPULevel CPULevel = Constants.DEFAULT_CPU_LEVEL;
    public static Language language = Constants.DEFAULT_LANGUAGE;
    public static int playersNum = Constants.DEFAULT_PLAYERS_NUMBER;
    public static final ArrayList<PlayerTO> playersTO;

    static {
        playersTO = new ArrayList<>();
        //The first player is the user
        playersTO.add(new PlayerTO(Constants.DEFAULT_PLAYER_NAME, true, null, true, false, Constants.DEFAULT_BLUFF_COEFF, 0, 0, 0));
        for (int i = 1; i < Constants.DEFAULT_PLAYERS_NUMBER; i++) {
            playersTO.add(new PlayerTO(NameGenerator.getRandomName(true), true, AvatarGenerator.getRandomAvatar(true), false, true, Constants.DEFAULT_BLUFF_COEFF, 0, 0, 0));
        }
    }
}
