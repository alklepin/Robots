package save;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StateManager {
    /**
     * Map with all variables and their values
     */
    private final Map<String, String> storage = new HashMap<>();

    /**
     * Parse component and insert values into the map
     *
     * @param attribute - class name
     * @param component - map of variable names and their values
     */
    public void storeFrame(String attribute, Component component) {
        Logger.debug(attribute + " memorize trigger");
        Point position = component.getLocation();
        storage.put(attribute + ".pos_x", position.x + "");
        storage.put(attribute + ".pos_y", position.y + "");

        Dimension size = component.getSize();
        storage.put(attribute + ".width", size.width + "");
        storage.put(attribute + ".height", size.height + "");
        if (component instanceof JInternalFrame frame) storage.put(attribute + ".icon", frame.isIcon() + "");
        Logger.debug(attribute + " memorize complete");
    }

    /**
     * Extract values and configure frame with them
     *
     * @param attribute - class name
     * @throws WindowInitException - exception occurred during window initialization
     */
    public void recoverFrame(String attribute, Component component) throws WindowInitException {
        Logger.debug(attribute + " dememorization trigger");
        Map<String, String> values = new HashMap<>();
        for (String key : storage.keySet()) {
            if (key.startsWith(attribute)) {
                values.put(key.substring(attribute.length() + 1), storage.get(key));
            }
        }
        try {
            component.setBounds(Integer.parseInt(values.get("pos_x")), Integer.parseInt(values.get("pos_y")),
                    Integer.parseInt(values.get("width")),
                    Integer.parseInt(values.get("height")));
            if (component instanceof JInternalFrame frame) frame.setIcon(values.get("icon").equals(true + ""));
            Logger.debug(attribute + " dememorization success");
        } catch (Exception e) {
            throw new WindowInitException(attribute + " dememorization failed due to exception with message:\n" + e.getMessage());
        }
    }

    /**
     * Extract all stored data
     *
     * @return {@link StateManager#storage}
     */
    protected Map<String, String> extractStorage() {
        return storage;
    }

    /**
     * Utility method for convenient storage filling in restore procedure
     *
     * @param name  - both attribute and variable
     * @param value - value of the variable
     */
    protected void put(String name, String value) {
        storage.put(name, value);
    }
}
