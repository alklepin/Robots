package robots.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import robots.domain.InternalWindow;

public class GameWindow extends InternalWindow
{
    private final GameVisualizer m_visualizer;
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        this.setSize(400, 400);
    }
}
