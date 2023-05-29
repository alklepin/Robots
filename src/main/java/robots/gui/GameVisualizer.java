package robots.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import robots.domain.DoublePoint;
import robots.domain.Robot;

public class GameVisualizer extends JPanel implements Observer {
    private final Timer m_timer = initTimer();

    private Robot robot;
    private Visualization visualization;

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public GameVisualizer(Robot robot) {
        this.robot = robot;
        this.robot.addObserver(this);
        visualization = new Visualization();

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                robot.onModelUpdateEvent();
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                robot.setTargetPosition(
                        new DoublePoint(point.x * 2, point.y * 2)
                );
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void update(Observable o, Object arg) {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        visualization.drawRobot((Graphics2D) g, robot);
        visualization.drawTarget((Graphics2D) g, robot.getTargetPosition());
    }
}
