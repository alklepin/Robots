package ru.kemichi.robots.gui.windows;

import org.json.simple.JSONObject;

import javax.swing.*;

abstract class AbstractWindow extends JInternalFrame {
    private String configurationPath = "";

    public AbstractWindow(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
        this.configurationPath = "";
    }

    public void setConfigurationPath(String savePath) {
        this.configurationPath = savePath;
    }

    public String getConfigurationPath() {
        return configurationPath;
    }

    public JSONObject parseConfiguration() {
        JSONObject configuration = new JSONObject();
        configuration.put("x", this.getX());
        configuration.put("y", this.getY());
        configuration.put("width", this.getWidth());
        configuration.put("height", this.getHeight());
        configuration.put("isIcon", this.isIcon());
        return configuration;
    }

    public void applyConfiguration(JSONObject configuration) {

    }

}
