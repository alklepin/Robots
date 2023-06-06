package robots.domain.windows;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import robots.domain.windows.InternalWindow;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public abstract class InternalWindowJsonConfigurable extends InternalWindow {
    public InternalWindowJsonConfigurable(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }
    private void setConfiguration(JSONObject configuration) {
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

    protected abstract String getSavePath();

    public void saveConfiguration() {
        File configPath = Paths.get(System.getProperty("user.home"), ".Robots", this.getSavePath()).toFile();
        try {
            configPath.getParentFile().mkdirs();
            configPath.createNewFile();
            FileWriter writer = new FileWriter(configPath);
            writer.write(this.getConfiguration().toJSONString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfiguration() {
        this.setConfiguration(this.readConfiguration(this.getSavePath()));
    }
}
