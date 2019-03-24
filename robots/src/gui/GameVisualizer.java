package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();
    
    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public Wall[] walls = new Wall[]{new Wall(300, 200,310, 300),
            new Wall(300, 300,410, 310), new Wall(400, 200, 410, 300),
            new Wall(200, 200,300, 210), new Wall(800, 200,810, 300),
            new Wall(800, 300,900, 310), new Wall(900, 300,910, 400),
            new Wall(900, 400,1000, 410), new Wall(600, 800,700, 810),
            new Wall(300, 700,310, 800)};
    public Mine[] mines = new Mine[]{new Mine(15, 600), new Mine(590, 790), new Mine(145, 145),
            new Mine(800, 183), new Mine(1000, 430)};
    
    private volatile double m_robotPositionX;
    private volatile double m_robotPositionY;
    private volatile double m_robotDirection;

    private volatile int m_targetPositionX;
    private volatile int m_targetPositionY;
    
    private static final double maxVelocity = 0.1; 
    private static final double maxAngularVelocity = 0.001; 
    
    public GameVisualizer() 
    {
        setStartGame();
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

    public void setStartGame()
    {
        m_robotPositionX = 320;
        m_robotPositionY = 220;
        m_robotDirection = 1;
        m_targetPositionX = 321;
        m_targetPositionY = 320;
    }

    protected void setTargetPosition(Point p)
    {
        for(int i = 0; i < walls.length; i++)
        {
            if (p.x >= walls[i].Position.x & p.x <= walls[i].SecondPosition.x & p.y >= walls[i].Position.y & p.y <= walls[i].SecondPosition.y)
            {
                return;
            }
        }
        for (int i = 0; i < mines.length; i++)
            if (distance(p.x, p.y, mines[i].Position.x, mines[i].Position.y) <= 5)
                return;
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }
    
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }
    
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    
    protected void onModelUpdateEvent()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY, 
            m_robotPositionX, m_robotPositionY);
        if (distance < 0.1)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }
        
        moveRobot(velocity, angularVelocity, 10);
    }
    
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
    
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        velocity = applyLimits(velocity, 0, maxVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY)) {
            newY = m_robotPositionY + velocity *duration * Math.sin(m_robotDirection);
        }
        if (getWalls(newX, newY))
        {
            if (newX > m_robotPositionX)
                m_robotPositionX = m_robotPositionX - 1;
            else
                m_robotPositionX = m_robotPositionX + 1;
            m_robotDirection = m_robotDirection + 0.09;
        }
        else
        {
            if (getMines(newX, newY))
            {
                int n = JOptionPane.showConfirmDialog(this,

                        "You died",

                        "Game over", JOptionPane.DEFAULT_OPTION);
                switch (n)
                {
                    case JOptionPane.YES_OPTION: setStartGame(); break;
                    case JOptionPane.CLOSED_OPTION: setStartGame(); break;
                    default:
                }

            }
            else
            {
                m_robotPositionX = newX;
                m_robotPositionY = newY;
                double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
                m_robotDirection = newDirection;
            }
        }
    }

    public boolean getWalls(double x, double y) {
        for (int i = 0; i < walls.length; i ++) {
            if (x > walls[i].Position.x & x < walls[i].SecondPosition.x & y > walls[i].Position.y & y < walls[i].SecondPosition.y)
                return true;
        }
        return false;
    }

    public boolean getMines(double x, double y) {
        for (int i = 0; i < mines.length; i++)
        {
            if (distance(x, y, mines[i].Position.x, mines[i].Position.y) <= 5)
                return true;
        }
        return false;
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    private static int round(double value)
    {
        return (int)(value + 0.5);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        for(int i = 0; i < walls.length; i++)
        {
            walls[i].draw(g2d);
        }
        for(int i = 0; i < mines.length; i++)
        {
            mines[i].draw(g2d);
        }
        drawRobot(g2d, round(m_robotPositionX), round(m_robotPositionY), m_robotDirection);
        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = round(m_robotPositionX); 
        int robotCenterY = round(m_robotPositionY);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); 
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX - 12, robotCenterY,24 , 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX - 12, robotCenterY, 24, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  - 5, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  - 5, robotCenterY, 5, 5);
    }
    
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 9, 9);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 9, 9);
    }
}
