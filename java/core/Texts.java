package core;

import java.util.ResourceBundle;

public class Texts {
    private static final ResourceBundle resourceBundle;
    static {
        String language = (String) UserDataSaver.getUserDataSaver().loadObject("language");
        if (language == null)
            language = "lang_en";
        resourceBundle = ResourceBundle.getBundle(language);
    }

    public static String get(String key) {
        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        }

        return key;
    }

    public static void setLanguage(String lang) {
        UserDataSaver.getUserDataSaver().saveObject("language", lang);
    }
}
