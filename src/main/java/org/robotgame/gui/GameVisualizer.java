package org.robotgame.gui;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class GameVisualizer extends JPanel {
    private final Timer m_timer = initTimer();
    private static final double maxVelocity = 0.1;


    private volatile double m_robotDirection = 0;
    private volatile int m_robotPositionX = 100;
    private volatile int m_robotPositionY = 100;
    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    public GameVisualizer() {
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 10);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 20);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                switch (keyCode) {
                    case KeyEvent.VK_W: // Движение вверх
                        m_targetPositionY -= 20;
                        break;
                    case KeyEvent.VK_A: // Движение влево
                        m_targetPositionX -= 20;
                        break;
                    case KeyEvent.VK_S: // Движение вниз
                        m_targetPositionY += 20;
                        break;
                    case KeyEvent.VK_D: // Движение вправо
                        m_targetPositionX += 20;
                        break;
                    case KeyEvent.VK_UP:
                        m_targetPositionY -= 20; // Движение вверх
                        break;
                    case KeyEvent.VK_LEFT:
                        m_targetPositionX -= 20; // Движение влево
                        break;
                    case KeyEvent.VK_DOWN:
                        m_targetPositionY += 20; // Движение вниз
                        break;
                    case KeyEvent.VK_RIGHT:
                        m_targetPositionX += 20; // Движение вправо
                        break;
                }
                repaint(); // Перерисовываем панель
            }


            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });

        setDoubleBuffered(true);
        setFocusable(true);
    }


    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    protected void setTargetPosition(Point p) {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    protected void onModelUpdateEvent() {
        double distance = distance(m_targetPositionX, m_targetPositionY, m_robotPositionX, m_robotPositionY);

        if (distance < 0.5) {
            return;
        }

        double angleToTarget = asNormalizedRadians(angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY));
        moveRobot(maxVelocity, angleToTarget, 10);

        m_robotPositionX = (int) applyLimits(m_robotPositionX, 0, getWidth());
        m_robotPositionY = (int) applyLimits(m_robotPositionY, 0, getHeight());

        m_targetPositionX = (int) applyLimits(m_targetPositionX, 0, getWidth());
        m_targetPositionY = (int) applyLimits(m_targetPositionY, 0, getHeight());
    }

    private double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private static double distance(int x1, int y1, int x2, int y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(int fromX, int fromY, int toX, int toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        double angle = Math.atan2(diffY, diffX);

        // Убедитесь, что угол находится в отрезке [0, 2*PI]
        if (angle < 0) {
            angle += 2 * Math.PI;
        }

        return angle;
    }

    private void moveRobot(double velocity, double angleToTarget, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);

        int deltaX = (int) Math.round(velocity * Math.cos(angleToTarget) * duration);
        int deltaY = (int) Math.round(velocity * Math.sin(angleToTarget) * duration);

        m_robotPositionX += deltaX;
        m_robotPositionY += deltaY;

        m_robotDirection = angleToTarget;
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawRobot(g, m_robotPositionX, m_robotPositionY, m_robotDirection);
        drawTarget(g, m_targetPositionX, m_targetPositionY);
    }

    private void drawRobot(Graphics g, int x, int y, double direction) {
        int robotCenterX = x;
        int robotCenterY = y;

        int noseLength = 8; // Смещение носика
        int noseX = robotCenterX + (int) (noseLength * Math.cos(direction));
        int noseY = robotCenterY + (int) (noseLength * Math.sin(direction));

        // Рисуем тело робота
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 20, 10, m_robotDirection);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 20, 10, m_robotDirection);

        // Рисуем носик
        g.setColor(Color.WHITE);
        fillOval(g, noseX, noseY, 5, 5, m_robotDirection);
        g.setColor(Color.BLACK);
        drawOval(g, noseX, noseY, 5, 5, m_robotDirection);
    }

    private void drawTarget(Graphics g, int x, int y) {
        double angleToTarget = asNormalizedRadians(angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY));

        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5, angleToTarget);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5, angleToTarget);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2, double angle) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(angle, centerX, centerY);
        g2d.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2); // Рисуем овал
        g2d.dispose();
    }


    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2, double angle) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.rotate(angle, centerX, centerY);
        g2d.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
        g2d.dispose();
    }

}
