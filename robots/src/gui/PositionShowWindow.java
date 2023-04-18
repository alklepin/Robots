package gui;

import models.RobotModel;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class PositionShowWindow extends JInternalFrame implements Observer {
    private RobotModel m_model;
    private JLabel m_labelX;
    private JLabel m_labelY;
    public PositionShowWindow(RobotModel model){
        super("Labels", true, true, true, true);
        m_model=model;
        m_labelX=new JLabel("X : %f".formatted(m_model.getM_PositionX()));
        m_labelY=new JLabel("T : %f".formatted(m_model.getM_PositionY()));
        m_model.addObserver(this);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_labelX, BorderLayout.CENTER);
        panel.add(m_labelY, BorderLayout.AFTER_LAST_LINE);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void update(Observable o, Object arg) {
        m_labelX.setText("X : %f".formatted(m_model.getM_PositionX()));
        m_labelY.setText("Y : %f".formatted(m_model.getM_PositionY()));
    }
}
