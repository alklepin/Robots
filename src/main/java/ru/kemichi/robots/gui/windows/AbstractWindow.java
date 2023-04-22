package ru.kemichi.robots.gui.windows;

import org.json.simple.JSONObject;

import javax.swing.*;
import java.beans.PropertyVetoException;

public abstract class AbstractWindow extends JInternalFrame implements Configurable{
    private String configurationPath = "";

    public AbstractWindow(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    public void setConfigurationPath(String savePath) {
        this.configurationPath = savePath;
    }

    public String getConfigurationPath() {
        return configurationPath;
    }

    public JSONObject extractConfiguration() {
        JSONObject configuration = new JSONObject();
        configuration.put("x", this.getX());
        configuration.put("y", this.getY());
        configuration.put("width", this.getWidth());
        configuration.put("height", this.getHeight());
        configuration.put("isIcon", this.isIcon());
        return configuration;
    }

    public void applyConfiguration(JSONObject configuration) {
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

}
