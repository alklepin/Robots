package gui;

import controllers.TargetPositionController;
import gui.drawModels.RobotRepresentation;
import gui.drawModels.TargetRepresentation;

import models.RobotModel;
import models.TargetModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel implements Observer{



    private RobotRepresentation m_robotView;
    private TargetRepresentation m_targetView;

    private TargetPositionController m_targetController;
    private RobotModel m_robot;




    public GameVisualizer(TargetPositionController modelController, RobotModel model, TargetModel target) {

        m_robot =model;
        m_robotView = new RobotRepresentation(m_robot);
        m_targetView = new TargetRepresentation(target);
        m_targetController = modelController;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    protected void setTargetPosition(Point p) {


        m_targetController.setTargetPos(p);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }


    protected void onModelUpdateEvent() {
        onRedrawEvent();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        m_robotView.draw(g2d);
        m_targetView.draw(g2d);

    }


    @Override
    public void update(Observable o, Object arg) {
        EventQueue.invokeLater(this::onModelUpdateEvent);
    }



}
