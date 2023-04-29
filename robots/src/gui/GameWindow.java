package gui;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrame
{

    public GameWindow(RobotController controller)
    {
        super("Игровое поле", true, true, true, true);
        GameVisualizer m_visualizer = new GameVisualizer(controller);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
