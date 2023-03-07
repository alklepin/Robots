package org.iffomko.gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * Окно с игрой
 */
public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;

    /**
     * Создает окно с игрой
     */
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
