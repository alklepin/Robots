package ru.kemichi.robots.gui.windows;

import org.json.simple.JSONObject;

public interface Configurable {
    void setConfigurationPath(String path);
    String getConfigurationPath();
    JSONObject extractConfiguration();
    void applyConfiguration(JSONObject configuration);
}
