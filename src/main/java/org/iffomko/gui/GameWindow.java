package org.iffomko.gui;

import org.iffomko.savers.Savable;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.xml.stream.Location;

/**
 * Окно с игрой
 */
public class GameWindow extends JInternalFrame implements Savable
{
    private final GameVisualizer m_visualizer;
    private final String prefix;
    private Map<String, String> state;

    /**
     * Создает окно с игрой
     */
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);

        m_visualizer = new GameVisualizer();

        setSize(new Dimension(400, 400));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        prefix = "game";

        state = new HashMap<>();
    }

    /**
     * <p>Возвращает состояние настроек класса, который нужно сохранить</p>
     *
     * @return - любой объект, который реализует интерфейс <code>Map</code>
     */
    @Override
    public Map<String, String> save() {
        if (state.get("isIconified") == null) {
            state.put("isIconified", String.valueOf(isIcon()));
        } else {
            state.replace("isIconified", String.valueOf(isIcon()));
        }

        double x = getLocation().getX();
        double y = getLocation().getY();
        int width = getWidth();
        int height = getHeight();

        if (state.get("x") == null) {
            state.put("x", String.valueOf(x));
        } else {
            state.replace("x", String.valueOf(x));
        }

        if (state.get("y") == null) {
            state.put("y", String.valueOf(y));
        } else {
            state.replace("y", String.valueOf(y));
        }

        if (state.get("width") == null) {
            state.put("width", String.valueOf(width));
        } else {
            state.replace("width", String.valueOf(width));
        }

        if (state.get("height") == null) {
            state.put("height", String.valueOf(height));
        } else {
            state.replace("height", String.valueOf(height));
        }

        return state;
    }

    /**
     * <p>Восстанавливает состояние объекта исходя из состояния, которое пришло ему извне</p>
     *
     * @param state - состояние, которое нужно восстановить
     */
    @Override
    public void restore(Map<String, String> state) {
        try {
            this.state = state;

            int width = Integer.parseInt(this.state.get("width"));
            int height = Integer.parseInt(this.state.get("height"));
            double x = Double.parseDouble(this.state.get("x"));
            double y = Double.parseDouble(this.state.get("y"));
            boolean isIconified = Boolean.parseBoolean(this.state.get("isIconified"));

            setSize(new Dimension(width, height));

            Point gameLocation = new Point();
            gameLocation.setLocation(x, y);

            setLocation(gameLocation);
            setIcon(isIconified);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Возвращает префикс, который ассоциируется с этим классом</p>
     *
     * @return - префикс
     */
    @Override
    public String getPrefix() {
        return prefix;
    }
}
