package org.iffomko.gui;

import org.iffomko.savers.ComponentSaver;

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

import javax.swing.JPanel;

/**
 * Основная логика игры
 */
public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();

    /**
     * Создается таймер, который называется генератор событий
     * @return - таймер
     */
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

    public GameVisualizer()
    {
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50); // добавляет задачу в таймер каждый 50 миллисекунд перерисовывать игру
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

    /**
     * Устанавливает позицию для элемента, которому стремится робот
     * @param p - точка, к которой робот стремится
     */
    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    /**
     * Добавляет в очередь событий перерисовку игры
     */
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    /**
     * Высчитывает расстояние между двумя точками (x1, y1) и (x2, y2)
     * @param x1 - координата x первой точки
     * @param y1 - координата y первой точки
     * @param x2 - координата x второй точки
     * @param y2 - координата y второй точки
     * @return - расстояние
     */
    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Вычисляет тета-угол между двумя точками
     * @param fromX - координата по оси OX первой точки
     * @param fromY - координата по оси OY первой точки
     * @param toX - координата по оси OX второй точки
     * @param toY - координата по оси OY второй точки
     * @return - тета-угол
     */
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    /**
     * Метод, который вызывается каждый раз для обновления координат моделей
     */
    protected void onModelUpdateEvent()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY); // расстояние между точкой назначения
        if (distance < 0.5) {
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

    /**
     * Приводит входное значение <code>value</code> к нормализованным значениям, т. е. чтобы не вышло за границы <code>[min, max]</code>
     * @param value - значение, которое нужно привести
     * @param min - минимальное значение, которое может принимать <code>value</code>
     * @param max - максимальное значение, которое может принимать <code>value</code>
     * @return - приведенное значение
     */
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    /**
     * Передвигает робота
     * @param velocity - линейная скорость передвижения робота
     * @param angularVelocity - угловая скорость передвижения робота
     * @param duration - промежуток, в течении которого он двигается
     */
    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity); // нормализуем обычную скорость робота
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity); // Нормализуем угловую скорость робота, т. е. скорость при вращении
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) - Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration); // меняем направление робота
        m_robotDirection = newDirection;
    }

    /**
     * Делает угол положительным (0 <= angle < 2*Math.PI)
     * @param angle - входящий угол в радианах
     * @return - нормализованный угол
     */
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

    /**
     * Округляет значение в большую сторону
     * @param value - входное значение
     * @return - округленное значение
     */
    private static int round(double value)
    {
        return (int)(value + 0.5);
    }

    /**
     * Отрисовывает графику игры
     * @param g -  <code>Graphics</code> контекст, в котором нужно отрисовывать игру
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawRobot(g2d, round(m_robotPositionX), round(m_robotPositionY), m_robotDirection);
        drawTarget(g2d, m_targetPositionX, m_targetPositionY);
    }

    /**
     * Рисует овал без контура, только внутренняя часть
     * @param g - компонента, где надо отрисовать сам овал
     * @param centerX - координата оси OX, вокруг которой надо отрисовать овал
     * @param centerY - координата оси OY, вокруг которой надо отрисовать овал
     * @param diam1 - первый диаметр овала
     * @param diam2 - второй диаметр овала
     */
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Рисует контур овала
     * @param g - компонента, где надо отрисовать контур овала
     * @param centerX - координата оси OX, в которой надо отрисовать овал
     * @param centerY - координата оси OY, в которой надо отрисовать овал
     * @param diam1 - первый диаметр овала
     * @param diam2 - второй диаметр овала
     */
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Рисует робота
     * @param g - компонента, где нужно отрисовать робота
     * @param x - координата оси OX, в которой надо отрисовать робота
     * @param y - координата оси OY, в которой надо отрисовать робота
     * @param direction - угол поворота в радианах
     */
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = round(m_robotPositionX);
        int robotCenterY = round(m_robotPositionY);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); // поворачивает
        // робота на угол direction относительно координат robotCenterX, robotCenterY

        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }

    /**
     * Рисует курсор, к которому стремится робот
     * @param g - компонента, нужно отрисовать овал
     * @param x - координата оси OX, в которой надо нарисовать овал
     * @param y - координата оси OY, в которой надо нарисовать овал
     */
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); // поворачивает на ноль градусов указатель
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}