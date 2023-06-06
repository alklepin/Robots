package robots.gui;

import robots.domain.windows.InternalWindow;
import robots.interfaces.Configurable;

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
