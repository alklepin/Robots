package robots.gui;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import robots.interfaces.Configurable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;


// перенести в друкой пакет?
public class Configurator {
    private ArrayList<Configurable> configurables = new ArrayList<>();

    public void addConfigurable(Configurable configurable) {
        configurables.add(configurable);
    }

    public void saveConfigurations() {
        for (Configurable configurable : configurables) {
            saveConfiguration(configurable, configurable.getSavePath());
        }
    }

    private JSONObject readConfiguration(String savePath) {
        File configFile = Paths.get(System.getProperty("user.home"), ".Robots", savePath).toFile();
        JSONParser parser = new JSONParser();
        if (configFile.exists()) {
            try {
                return (JSONObject) parser.parse(new FileReader(configFile));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void loadConfiguration(Configurable configurable) {
        JSONObject config = readConfiguration(configurable.getSavePath());
        if (config != null) {
            configurable.setConfiguration(config);
        }
    }

    private void saveConfiguration(Configurable configurable, String savePath) {
        File configPath = Paths.get(System.getProperty("user.home"), ".Robots", savePath).toFile();
        try {
            configPath.getParentFile().mkdirs();
            configPath.createNewFile();
            FileWriter writer = new FileWriter(configPath);
            writer.write(configurable.getConfiguration().toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
