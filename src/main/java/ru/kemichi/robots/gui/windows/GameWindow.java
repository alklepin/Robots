package ru.kemichi.robots.gui.windows;

import ru.kemichi.robots.gui.GameVisualizer;
import ru.kemichi.robots.models.Robot;

import java.awt.*;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameWindow extends AbstractWindow {

    private static Timer initTimer() {
        return new Timer("up to date window dimension", true);
    }
    public GameWindow(ResourceBundle bundle, Robot robot) {
        super("gameWindow/config.json", bundle.getString("gameWindowHeader"), true, true, true, true);
        Timer timer = initTimer();
        GameVisualizer visualizer = new GameVisualizer(robot);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Dimension size = panel.getSize();
                visualizer.setWindowDimension(size);
            }
        }, 0, 1);
    }

    @Override
    public void defaultWindowSetup() {
        this.setLocation(350, 10);
        this.pack();
        this.setSize(800, 800);
    }

}
