package gui;

import controllers.TargetPositionController;
import gui.serial.SerializableFrame;
import models.RobotModel;
import models.TargetModel;
import windowConstructors.GameWindowConstructor;
import windowConstructors.WindowConstructor;


import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements SerializableFrame
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


    @Override
    public WindowConstructor getFrameState() {
        return new GameWindowConstructor(getX(),getY(),getSize().width,getSize().height);
    }
}
