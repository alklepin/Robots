package org.iffomko.gui;

import org.iffomko.models.Robot;
import org.iffomko.models.Target;
import org.iffomko.savers.Savable;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.*;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * Окно с игрой
 */
public class GameWindow extends JInternalFrame implements Savable
{
    private final GameVisualizer gameVisualizer;
    private final Robot robot;
    private final Target target;
    private final ActualRobotPosition actualRobotPosition;
    private final String prefix;
    private Map<String, String> state;

    private final static Timer timer = initTimer();
    private final static int durationRedraw = 10;

    /**
     * Создается таймер, который называется генератор событий
     * @return - таймер
     */
    private static Timer initTimer() {
        Timer timer = new Timer("event generator", true);
        return timer;
    }

    /**
     * Создает окно с игрой
     */
    public GameWindow()
    {
        super("Игровое поле", true, true, true, true);

        robot = new Robot();
        target = new Target();
        gameVisualizer = new GameVisualizer(robot, target, durationRedraw);
        actualRobotPosition = new ActualRobotPosition(robot);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameVisualizer.onModelUpdateEvent();
            }
        }, 0, durationRedraw);

        robot.addObserver(actualRobotPosition);
        robot.addObserver(gameVisualizer);
        target.addObserver(gameVisualizer);

        setSize(new Dimension(400, 400));

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(actualRobotPosition, BorderLayout.NORTH);
        panel.add(gameVisualizer, BorderLayout.CENTER);

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
