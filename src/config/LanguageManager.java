package config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import miscellanea.Constants;

public class LanguageManager {

    private static final HashMap<String, String> translations = new HashMap<>();

    public static void loadTranslations() {
        translations.clear();
        BufferedReader in = null;
        try {
            String filename = Constants.LANGUAGE_FILE_PATH.replace(Constants.LANGUAGE_PLACEHOLDER, Settings.language.toString());
            in = new BufferedReader(new FileReader(filename));
            String buffer;
            String[] splits;
            while ((buffer = in.readLine()) != null) {
                splits = buffer.split(Constants.KEY_VALUE_SEPARATOR);
                translations.put(splits[0], splits[1]);
            }
            System.out.println("Translations loaded successfully (" + Settings.language + ").\n");
        } catch (IOException ex) {
            translations.clear();
            System.err.println("Unable to load translations. Default language will be used.\n");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
            }
        }
    }

    public static String getValue(String text) {
        String value = translations.get(text);
        return value == null ? text : value.replace("\\n", System.getProperty("line.separator"));
    }

}
