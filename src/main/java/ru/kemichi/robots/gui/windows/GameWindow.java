package ru.kemichi.robots.gui.windows;

import ru.kemichi.robots.gui.GameVisualizer;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JPanel;

public class GameWindow extends AbstractWindow {
    public GameWindow(ResourceBundle bundle) {
        super("gameWindow/config.json", bundle.getString("gameWindowHeader"), true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void defaultWindowSetup() {
        this.setLocation(350, 10);
        this.pack();
        this.setSize(800, 800);
    }

}
