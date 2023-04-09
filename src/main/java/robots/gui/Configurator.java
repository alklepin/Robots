package robots.gui;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


// перенести в друкой пакет?
public class Configurator {
    private JSONObject configuration = new JSONObject();

    public void loadConfiguration() {
        File configFile = new File(System.getProperty("user.home") + "/.Robots/config.json");
        JSONObject config = readJsonFile(configFile);
        if (config != null) {
            configuration = config;
        }
    }

    public void saveWindowConfiguration(JInternalFrame window, String name) {
        JSONObject windowConfig = new JSONObject();
        windowConfig.put("x", window.getX());
        windowConfig.put("y", window.getY());
        windowConfig.put("width", window.getWidth());
        windowConfig.put("height", window.getHeight());
        windowConfig.put("isIcon", window.isIcon());
        configuration.put(name, windowConfig);
    }

    public void saveConfiguration() {
        File configFile = new File(System.getProperty("user.home") + "/.Robots/config.json");
        try {
            configFile.getParentFile().mkdirs();
            configFile.createNewFile();
            FileWriter writer = new FileWriter(configFile);
            writer.write(configuration.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadWindowConfiguration(JInternalFrame window, String name) {
        JSONObject windowConfig = (JSONObject) configuration.get(name);
        if (windowConfig != null) {
            window.setBounds(
                    ((Long) windowConfig.get("x")).intValue(),
                    ((Long) windowConfig.get("y")).intValue(),
                    ((Long) windowConfig.get("width")).intValue(),
                    ((Long) windowConfig.get("height")).intValue()
            );
            if ((Boolean) windowConfig.get("isIcon")) {
                try {
                    window.setIcon(true);
                } catch (PropertyVetoException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private JSONObject readJsonFile(File file) {
        JSONParser parser = new JSONParser();
        if (file.exists()) {
            try {
                return (JSONObject) parser.parse(new FileReader(file));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
