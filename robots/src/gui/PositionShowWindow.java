package gui;


import gui.serial.InnerWindowStateContainer;
import gui.serial.SerializableFrame;
import models.RobotModel;
import models.states.RobotStateReader;
import windowConstructors.PositionShowConstructor;
import windowConstructors.WindowConstructor;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PositionShowWindow extends JInternalFrame implements Observer, SerializableFrame {
    private RobotModel m_model;
    private JLabel m_labelX;
    private JLabel m_labelY;
    public PositionShowWindow(RobotModel model){
        super("Robot coordinates", true, true, true, true);
        m_model=model;
        RobotStateReader state= m_model.getState();
        m_labelX=new JLabel("X : %f".formatted(state.getX()));
        m_labelY=new JLabel("T : %f".formatted(state.getY()));
        m_model.addObserver(this);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_labelX, BorderLayout.CENTER);
        panel.add(m_labelY, BorderLayout.AFTER_LAST_LINE);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void update(Observable o, Object arg) {
        EventQueue.invokeLater(this::onTextUpdate);
    }
    void onTextUpdate(){
        RobotStateReader state= m_model.getState();
        m_labelX.setText("X : %f".formatted(state.getX()));
        m_labelY.setText("Y : %f".formatted(state.getY()));
    }

    @Override
    public WindowConstructor getFrameState() {
        return new PositionShowConstructor(getX(),getY(),getSize().width,getSize().height);
    }
}
