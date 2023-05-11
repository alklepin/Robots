package gui;

import controllers.TargetPositionController;
import models.RobotModel;
import models.TargetModel;


import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    public GameWindow(RobotModel model, TargetPositionController controller, TargetModel target)
    {
        super("Игровое поле", true, true, true, true);

        m_visualizer = new GameVisualizer(controller,model,target);
        model.addObserver(m_visualizer);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
