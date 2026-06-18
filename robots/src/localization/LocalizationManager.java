package localization;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public class LocalizationManager {
    private static final String BASE_NAME = "Messages";
    private static LocalizationManager instance;
    private ResourceBundle bundle;
    private Locale currentLocale;
    private final ConcurrentHashMap<String, MessageFormat> formatCache = new ConcurrentHashMap<>();
    private final java.util.List<Runnable> listeners = new java.util.ArrayList<>();

    private LocalizationManager() {
        setLocale(new Locale("ru"));
    }

    public static LocalizationManager getInstance() {
        if (instance == null) {
            instance = new LocalizationManager();
        }
        return instance;
    }

    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        this.bundle = ResourceBundle.getBundle(BASE_NAME, locale);
        formatCache.clear();
        notifyListeners();
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }

    public String format(String key, Object... args) {
        MessageFormat format = formatCache.computeIfAbsent(key, k -> {
            String pattern = getString(k);
            return new MessageFormat(pattern, currentLocale);
        });
        return format.format(args);
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    public void removeListener(Runnable listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }
}
