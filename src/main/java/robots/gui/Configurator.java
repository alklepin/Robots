package robots.gui;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import robots.domain.InternalWindow;
import robots.domain.InternalWindowJsonConfigurable;
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
            configurable.saveConfiguration();
        }
    }

    public void loadConfiguration(InternalWindow window) {
        window.loadConfiguration();
    }
}
