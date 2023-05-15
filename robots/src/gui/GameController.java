package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import static gui.Constants.GameVisualizerConstants.*;

public class GameController extends JPanel {

    private final Robot robot;
    private final Target target;

    public GameController() {
        robot = new Robot(ROBOT_INITIAL_X_COORDINATE, ROBOT_INITIAL_Y_COORDINATE, ROBOT_INITIAL_DIRECTION);
        target = new Target(TARGET_INITIAL_X_COORDINATE, TARGET_INITIAL_Y_COORDINATE);
        java.util.Timer timer = new Timer(TIMER_NAME, true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, TIMER_DELAY, TIMER_REDRAW_PERIOD);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, TIMER_DELAY, TIMER_UPDATE_PERIOD);
        this.setFocusable(true);
        this.requestFocus();
        addKeyListener(new KeyEventListener(target));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                target.setxCoordinate(event.getPoint().x);
                target.setyCoordinate(event.getPoint().y);
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    private void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    private void onModelUpdateEvent() {
        double distance = MathModule.calculateDistance(target.getxCoordinate(), target.getyCoordinate(), robot.getxCoordinate(), robot.getyCoordinate());
        target.move();
        if (distance < ROBOT_STOP_DISTANCE) {
            return;
        }
        double angleToTarget = MathModule.calculateAngle(robot.getxCoordinate(), robot.getyCoordinate(), target.getxCoordinate(), target.getyCoordinate());
        robot.setAngularVelocity(0);
        if (angleToTarget > robot.getDirection()) {
            robot.setAngularVelocity(MAX_ANGULAR_VELOCITY);
        }
        if (angleToTarget < robot.getDirection()) {
            robot.setAngularVelocity(-MAX_ANGULAR_VELOCITY);
        }
        robot.move();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        robot.draw(g2d);
        target.draw(g2d);
    }
}
