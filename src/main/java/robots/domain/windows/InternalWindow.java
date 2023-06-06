package robots.domain.windows;

import org.json.simple.JSONObject;
import robots.interfaces.Configurable;
import javax.swing.*;

public abstract class InternalWindow extends JInternalFrame implements Configurable {
    public InternalWindow(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    protected JSONObject getConfiguration() {
        JSONObject windowConfig = new JSONObject();
        windowConfig.put("x", this.getX());
        windowConfig.put("y", this.getY());
        windowConfig.put("width", this.getWidth());
        windowConfig.put("height", this.getHeight());
        windowConfig.put("isIcon", this.isIcon());
        return windowConfig;
    }

    public abstract void load();
}
