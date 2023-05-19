package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import static gui.Constants.GameVisualizerConstants.*;
import static gui.MainApplicationFrame.updateStateRobot;

public class GameController extends JPanel {

    private final Robot robot;
    private final Target target;
    private boolean isMoving;

    public GameController(Robot robot, Target target) {
        this.robot = robot;
        this.target = target;
        isMoving = false;
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

            if (isMoving) {
                isMoving = false;
                updateStateRobot(isMoving);
            }
            return;
        }
        if (!isMoving) {
            isMoving = true;
            updateStateRobot(isMoving);
        }
        double angleToTarget = MathModule.calculateAngle(robot.getxCoordinate(), robot.getyCoordinate(), target.getxCoordinate(), target.getyCoordinate());
        if (angleToTarget == robot.getDirection()) {
            robot.setAngularVelocity(0);
        } else if (angleToTarget <= Math.PI) {
            if (angleToTarget > robot.getDirection() || robot.getDirection() > angleToTarget + Math.PI) {
                robot.setAngularVelocity(MAX_ANGULAR_VELOCITY);
            } else {
                robot.setAngularVelocity(-MAX_ANGULAR_VELOCITY);
            }
        }
        else {
            if (angleToTarget > robot.getDirection() && robot.getDirection() > angleToTarget - Math.PI) {
                robot.setAngularVelocity(MAX_ANGULAR_VELOCITY);
            } else {
                robot.setAngularVelocity(-MAX_ANGULAR_VELOCITY);
            }
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
