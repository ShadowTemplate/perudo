package miscellanea;

import config.Settings;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class NameGenerator {

    private static final ArrayList<String> maleNames = new ArrayList<>();
    private static final ArrayList<String> femaleNames = new ArrayList<>();

    public static void loadNames() {
        ArrayList<String> currList = maleNames;
        String path = Constants.NAMES_FILE_PATH
                .replace(Constants.LANGUAGE_PLACEHOLDER, Settings.language.toString())
                .replace(Constants.GENDER_PLACEHOLDER, Constants.GENDER_MALE);
        boolean success = load(currList, path);
        if (!success) {
            currList.clear();
            return;
        }

        currList = femaleNames;
        path = Constants.NAMES_FILE_PATH
                .replace(Constants.LANGUAGE_PLACEHOLDER, Settings.language.toString())
                .replace(Constants.GENDER_PLACEHOLDER, Constants.GENDER_FEMALE);

        success = load(currList, path);
        if (!success) {
            currList.clear();
        }
    }

    private static boolean load(ArrayList<String> names, String filename) {
        boolean success = false;
        names.clear();
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filename));
            String buffer;
            while ((buffer = in.readLine()) != null) {
                names.add(buffer);
            }
            System.out.println("Names loaded successfully from " + filename);
            success = true;
        } catch (IOException ex) {
            System.err.println("Unable to load names. Random names will be used.");
        } finally {
            try {
                in.close();
            } catch (IOException | NullPointerException ex) {
            }
        }
        return success;
    }

    public static String getRandomName(boolean man) {
        ArrayList<String> names = man ? maleNames : femaleNames;

        if (names.size() >= Settings.playersNum) {
            return names.get(Utility.random.nextInt(names.size()));
        }

        return "Player_" + Utility.random.nextInt(100);
    }
}
