package ru.kemichi.robots.gui.windows;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public abstract class AbstractWindow extends JInternalFrame implements Configurable {
    private final String configurationPath;

    public AbstractWindow(String savePath, String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        configurationPath = savePath;
    }

    private JSONObject extractConfiguration() {
        JSONObject configuration = new JSONObject();
        configuration.put("x", this.getX());
        configuration.put("y", this.getY());
        configuration.put("width", this.getWidth());
        configuration.put("height", this.getHeight());
        configuration.put("isIcon", this.isIcon());
        return configuration;
    }

    private void applyConfiguration(JSONObject configuration) {
        if (configuration != null) {
            this.setBounds(
                    ((Long) configuration.getOrDefault("x", 50)).intValue(),
                    ((Long) configuration.getOrDefault("y", 50)).intValue(),
                    ((Long) configuration.getOrDefault("width", 400)).intValue(),
                    ((Long) configuration.getOrDefault("height", 400)).intValue()
            );
            if ((Boolean) configuration.getOrDefault("isIcon", false)) {
                try {
                    this.setIcon(true);
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void defaultWindowSetup() {

    }

    public void save() {
        File configPath = Paths.get(System.getProperty("user.home"), ".Robots", configurationPath).toFile();
        try {
            configPath.getParentFile().mkdirs();
            configPath.createNewFile();
            FileWriter writer = new FileWriter(configPath);
            JSONObject a = extractConfiguration();
            writer.write(a.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject readConfiguration(String filename) {
        JSONParser parser = new JSONParser();
        File config = Paths.get(System.getProperty("user.home"), ".Robots", filename).toFile();
        if (config.exists()) {
            try {
                return (JSONObject) parser.parse(new FileReader(config));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void load() {
        JSONObject config = readConfiguration(configurationPath);
        if (config != null) {
            applyConfiguration(config);
        }
    }

}
