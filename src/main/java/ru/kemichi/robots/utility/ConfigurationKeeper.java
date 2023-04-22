package ru.kemichi.robots.utility;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.kemichi.robots.gui.windows.Configurable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ConfigurationKeeper {
    private final ArrayList<Configurable> configurableItems = new ArrayList<>();

    public void addNewConfigurableItem(Configurable configurable) {
        configurableItems.add(configurable);
    }

    private void saveConfiguration(Configurable configurable) {
        File configPath = Paths.get(System.getProperty("user.home"), ".Robots", configurable.getConfigurationPath()).toFile();
        try {
            configPath.getParentFile().mkdirs();
            configPath.createNewFile();
            FileWriter writer = new FileWriter(configPath);
            JSONObject a = configurable.extractConfiguration();
            writer.write(a.toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAllConfigurations() {
        for (Configurable configurable : configurableItems) {
            saveConfiguration(configurable);
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

    public void applyConfiguration(Configurable configurable) {
        JSONObject config = readConfiguration(configurable.getConfigurationPath());
        if (config != null) {
            configurable.applyConfiguration(config);
        }
    }
}
