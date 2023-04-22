package gui;

import Controllers.Controller;
import gui.drawModels.DefaultRobot;
import gui.drawModels.TargetDrawRepresentation;
import models.RobotModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel implements Observer {
    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }


    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;


    private DefaultRobot m_robotDraw;
    private TargetDrawRepresentation m_target;

    private Controller m_controller;
    private RobotModel m_model;


    public GameVisualizer(Controller modelController, RobotModel model,int targetX, int targetY) {
        m_targetPositionX=targetX;
        m_targetPositionY=targetY;
        m_model=model;
        m_robotDraw = new DefaultRobot(m_model.getM_PositionX(), m_model.getM_PositionY(),m_model.getM_Direction());
        m_target = new TargetDrawRepresentation(targetX, targetY);
        m_controller = modelController;
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
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
        m_target.update(p.x, p.y);
        m_controller.setTargetPos(p);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }


    protected void onModelUpdateEvent() {
        m_robotDraw.update(m_model.getM_PositionX(), m_model.getM_PositionY(), m_model.getM_Direction());

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
