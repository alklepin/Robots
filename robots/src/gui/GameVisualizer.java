package gui;

import gui.drawModels.DefaultRobot;
import gui.drawModels.TargetDrawRepresentation;
import models.RobotModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();
    
    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;
    
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    private DefaultRobot m_robotDraw;
    private TargetDrawRepresentation m_target;

    private RobotModel m_model;

    
    public GameVisualizer() 
    {
        m_robotDraw=new DefaultRobot(m_robotPositionX,m_robotPositionY,m_robotDirection);
        m_target=new TargetDrawRepresentation(m_targetPositionX,m_targetPositionY);
        m_model=new RobotModel(100,100,100,150,100);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
        m_target.update(p.x,p.y);
        m_model.setTargetPosition(p);
    }
    
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }


    
    protected void onModelUpdateEvent()
    {

        m_model.updatePos();
        m_robotDraw.update(m_model.getM_PositionX(),m_model.getM_PositionY(),m_model.getM_Direction());

    }
    



    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        m_robotDraw.draw(g2d);
        m_target.draw(g2d);

    }


    

}
