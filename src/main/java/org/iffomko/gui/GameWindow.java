package org.iffomko.gui;

import org.iffomko.models.Robot;
import org.iffomko.models.Target;
import org.iffomko.savers.Savable;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private final RobotPositionPanel robotPositionPanel;
    private final String prefix;

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

        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "org.iffomko.gui.localizationProperties.gameWindow.GameWindowResource",
                new Locale(System.getProperty("user.language"), System.getProperty("user.country"))
        );

        this.setTitle(resourceBundle.getString("gameWindowTitle"));

        robot = new Robot();
        target = new Target();
        gameVisualizer = new GameVisualizer(robot, target, durationRedraw);
        robotPositionPanel = new RobotPositionPanel(robot);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameVisualizer.onModelUpdateEvent();
            }
        }, 0, durationRedraw);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                target.setTargetPosition(new Point(
                        e.getX() - (getWidth() - gameVisualizer.getWidth()) / 2,
                        e.getY() + (getHeight() - gameVisualizer.getHeight() - robotPositionPanel.getHeight()) / 4
                                - robotPositionPanel.getHeight()
                                - (getHeight() - gameVisualizer.getHeight() - robotPositionPanel.getHeight()))
                );
                repaint();
            }
        });

        robot.addObserver(robotPositionPanel);
        robot.addObserver(gameVisualizer);
        target.addObserver(gameVisualizer);

        setSize(new Dimension(400, 400));

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(robotPositionPanel, BorderLayout.NORTH);
        panel.add(gameVisualizer, BorderLayout.CENTER);

        getContentPane().add(panel);
        pack();

        prefix = "game";
    }

    /**
     * <p>Возвращает состояние настроек класса, который нужно сохранить</p>
     *
     * @return - любой объект, который реализует интерфейс <code>Map</code>
     */
    @Override
    public Map<String, String> save() {
        Map<String, String> state = new HashMap<>();

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
            int width = Integer.parseInt(state.get("width"));
            int height = Integer.parseInt(state.get("height"));
            double x = Double.parseDouble(state.get("x"));
            double y = Double.parseDouble(state.get("y"));
            boolean isIconified = Boolean.parseBoolean(state.get("isIconified"));

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
