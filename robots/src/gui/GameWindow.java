package gui;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    private final ResourceBundle localization;
    public GameWindow(ResourceBundle localization)
    {
        super(localization.getString("gameField"), true, true, true, true);
        this.localization = localization;
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
