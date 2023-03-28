package org.iffomko.gui;

import org.iffomko.robot.Robot;
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
    private final Robot robot;

    /**
     * Создается таймер, который называется генератор событий
     * @return - таймер
     */
    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    public GameVisualizer(Robot robot)
    {
        this.robot = robot;

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
     * Метод, который вызывается каждый раз для обновления координат моделей
     */
    protected void onModelUpdateEvent()
    {
        robot.move(m_targetPositionX, m_targetPositionY, 10);
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
        drawRobot(g2d, round(robot.getX()), round(robot.getY()), robot.getDirection());
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
        int robotCenterX = round(robot.getX());
        int robotCenterY = round(robot.getY());
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