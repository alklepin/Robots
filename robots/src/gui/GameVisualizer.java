package gui;

import controllers.ModelPositionController;
import controllers.ModelUpdateController;
import gui.drawModels.DefaultRobot;
import gui.drawModels.TargetDrawRepresentation;
import models.RobotModel;
import models.TargetModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel implements Observer {

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;


    private DefaultRobot m_robotDraw;
    private TargetDrawRepresentation m_target;

    private ModelPositionController m_controller;
    private RobotModel m_model;




    public GameVisualizer(ModelPositionController modelController, RobotModel model, TargetModel target) {

        m_model=model;
        m_robotDraw = new DefaultRobot(m_model.getM_PositionX(), m_model.getM_PositionY(),m_model.getM_Direction());
        m_target = new TargetDrawRepresentation(target);
        m_controller = modelController;

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
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;

        m_controller.setTargetPos(p);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }


    protected void onModelUpdateEvent() {
        m_robotDraw.update(m_model.getM_PositionX(), m_model.getM_PositionY(), m_model.getM_Direction());
        onRedrawEvent();
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        m_robotDraw.draw(g2d);
        m_target.draw(g2d);

    }


    @Override
    public void update(Observable o, Object arg) {
        EventQueue.invokeLater(this::onModelUpdateEvent);
    }

}
