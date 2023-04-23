package robots.domain;

import org.json.simple.JSONObject;
import robots.interfaces.Configurable;

import javax.swing.*;
import java.beans.PropertyVetoException;

public abstract class InternalWindow extends JInternalFrame implements Configurable {
    public InternalWindow(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }
    public void setConfiguration(JSONObject configuration) {
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

    public JSONObject getConfiguration() {
        JSONObject windowConfig = new JSONObject();
        windowConfig.put("x", this.getX());
        windowConfig.put("y", this.getY());
        windowConfig.put("width", this.getWidth());
        windowConfig.put("height", this.getHeight());
        windowConfig.put("isIcon", this.isIcon());
        return windowConfig;
    }

    public void setConfigurationSavePath(String savePath) {
        this.savePath = savePath;
    }

    public void Load() {}
}
