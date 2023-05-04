package gui;

import controllers.ModelUpdateController;
import models.RobotModel;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    public GameWindow(RobotModel model, ModelUpdateController controller)
    {
        super("Игровое поле", true, true, true, true);

        m_visualizer = new GameVisualizer(controller,model,150,100);
        model.addObserver(m_visualizer);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
