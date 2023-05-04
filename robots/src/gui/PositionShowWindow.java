package gui;

import models.CoordPair;
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
        super("Robot coordinates", true, true, true, true);
        m_model=model;
        CoordPair coord= m_model.getPos();
        m_labelX=new JLabel("X : %f".formatted(coord.x));
        m_labelY=new JLabel("T : %f".formatted(coord.y));
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
        CoordPair coord= m_model.getPos();
        m_labelX.setText("X : %f".formatted(coord.x));
        m_labelY.setText("Y : %f".formatted(coord.y));
    }
}
