package gui;

import controllers.TargetPositionController;
import gui.serial.InnerWindowStateContainer;
import gui.serial.MySerializable;
import models.RobotModel;
import models.TargetModel;


import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements MySerializable
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
    public InnerWindowStateContainer getState(){
        return new InnerWindowStateContainer(getX(),getY(),getSize().width,getSize().height);
    }

}
