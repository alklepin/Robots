package robots.interfaces;

import org.json.simple.JSONObject;

public interface Configurable {
    JSONObject getConfiguration();
    void setConfiguration(JSONObject configuration);
}
