package gui;

import controllers.ModelPositionController;
import gui.drawModels.DefaultRobot;
import gui.drawModels.TargetDrawRepresentation;
import models.Vector;
import models.RobotModel;
import models.TargetModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;

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
        Vector modelCoord=model.getPos();
        m_robotDraw = new DefaultRobot(modelCoord.x, modelCoord.y, modelCoord.z);
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
        Vector coord=m_model.getPos();
        m_robotDraw.update(coord.x, coord.y, coord.z);
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
