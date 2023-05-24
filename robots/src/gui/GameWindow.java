package gui;

import java.awt.BorderLayout;
import java.util.Observable;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame {
    private final GameDrawer m_drawer;
    private final RobotModel m_robot;

    public GameWindow(Parameters parameters) {
        super("Игровое поле", true, true, true, true);
        m_robot = new RobotModel(parameters);
        m_drawer = new GameDrawer(parameters);
        parameters.addObserver(m_drawer);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_drawer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}