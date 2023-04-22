package ru.kemichi.robots.gui.windows;

import ru.kemichi.robots.gui.GameVisualizer;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends AbstractWindow
{
    public GameWindow(ResourceBundle bundle)
    {
        super(bundle.getString("gameWindowHeader"), true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
