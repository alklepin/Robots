package gui;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Config {
    private final ResourceBundle localization;
    private final String path;
    private final Properties properties;

    public Config(String configPath, String localizationPath) {
        path = configPath;
        properties = loadProperties(configPath);
        localization = loadLocalization(localizationPath);
    }

    public boolean windowConfigsArePresent(JInternalFrame[] frames) {
        for (var frame: frames) {
            if (properties.containsKey(frame.getClass().getName() + "Minimized"))
                return true;
        }
        return false;
    }

    private Properties loadProperties(String path) {

        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream(path);
            property.load(fis);
        } catch (IOException e) {
            System.err.println("Error! Config file not found");
            System.exit(5);
        }

        return property;
    }

    public void saveWindowStates(JInternalFrame[] frames) {
        for (var frame: frames)
            saveWindowState(frame, frame.getClass().getName());
        save();
    }

    private void saveWindowState(JInternalFrame window, String name) {
        if (!window.isClosed()) {
            properties.setProperty(name + "Minimized", String.valueOf(window.isIcon()));
            properties.setProperty(name + "PositionX", String.valueOf(window.getX()));
            properties.setProperty(name + "PositionY", String.valueOf(window.getY()));
        }
    }

    public void setLocale(String lang, String country) {
        properties.setProperty("lang", lang);
        properties.setProperty("country", country);
        save();
    }

    public String getLocalization(String key) {
        return localization.getString(key);
    }

    private void save()
    {
        try {
            properties.store(new FileOutputStream(path), null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadWindowStates(JInternalFrame[] frames)
    {
        for (var frame: frames) {
            var name = frame.getClass().getName();
            if (properties.containsKey(name + "Minimized"))
                loadWindowConfig(frame, name);
        }
    }

    protected void loadWindowConfig(JInternalFrame window, String name) {
        var isMinimized = Boolean.parseBoolean(properties.getProperty(name + "Minimized"));
        var x = Integer.parseInt(properties.getProperty(name + "PositionX"));
        var y = Integer.parseInt(properties.getProperty(name + "PositionY"));
        try {
            window.setIcon(isMinimized);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        window.setLocation(x, y);
    }

    protected ResourceBundle loadLocalization(String localizationPath)
    {
        return ResourceBundle.getBundle(localizationPath,
                new Locale(properties.getProperty("lang"), properties.getProperty("country")));
    }
}
