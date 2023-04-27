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

import javax.swing.JPanel;

import static gui.Constants.GameVisualizerConstants.*;

public class GameVisualizer extends JPanel {
    private final Robot robot;
    private final Target target;

    public GameVisualizer() {
        robot = new Robot(ROBOT_INITIAL_X_COORDINATE, ROBOT_INITIAL_Y_COORDINATE, ROBOT_INITIAL_DIRECTION);
        target = new Target(TARGET_INITIAL_X_COORDINATE, TARGET_INITIAL_Y_COORDINATE);
        Timer timer = new Timer(TIMER_NAME, true);
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
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                setTargetPosition(event.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    private void setTargetPosition(Point point) {
        target.setXCoordinate(point.x);
        target.setYCoordinate(point.y);
    }

    private void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    private double calculateDistance(double firstPointXCoordinate, double firstPointYCoordinate, double secondPointXCoordinate, double secondPointYCoordinate) {
        double xCoordinateDifference = firstPointXCoordinate - secondPointXCoordinate;
        double yCoordinateDifference = firstPointYCoordinate - secondPointYCoordinate;
        return Math.sqrt(xCoordinateDifference * xCoordinateDifference + yCoordinateDifference * yCoordinateDifference);
    }

    private double calculateAngle(double startXCoordinate, double startYCoordinate, double endXCoordinate, double endYCoordinate) {
        double xCoordinateDifference = endXCoordinate - startXCoordinate;
        double yCoordinateDifference = endYCoordinate - startYCoordinate;
        return asNormalizedRadians(Math.atan2(yCoordinateDifference, xCoordinateDifference));
    }

    private void onModelUpdateEvent() {
        double distance = calculateDistance(target.getXCoordinate(), target.getYCoordinate(), robot.getXCoordinate(), robot.getYCoordinate());
        if (distance < ROBOT_STOP_DISTANCE) {
            return;
        }
        double angleToTarget = calculateAngle(robot.getXCoordinate(), robot.getYCoordinate(), target.getXCoordinate(), target.getYCoordinate());
        double angularVelocity = 0;
        if (angleToTarget > robot.getDirection()) {
            angularVelocity = MAX_ANGULAR_VELOCITY;
        }
        if (angleToTarget < robot.getDirection()) {
            angularVelocity = -MAX_ANGULAR_VELOCITY;
        }

        moveRobot(angularVelocity);
    }

    private void moveRobot(double angularVelocity) {
        double newRobotDirection = robot.getDirection() + angularVelocity * TIMER_UPDATE_PERIOD;
        double newRobotXCoordinate = robot.getXCoordinate() + ROBOT_VELOCITY / angularVelocity *
                (Math.sin(newRobotDirection) - Math.sin(robot.getDirection()));
        if (!Double.isFinite(newRobotXCoordinate)) {
            newRobotXCoordinate = robot.getXCoordinate() + ROBOT_VELOCITY * TIMER_UPDATE_PERIOD * Math.cos(robot.getDirection());
        }
        double newRobotYCoordinate = robot.getYCoordinate() - ROBOT_VELOCITY / angularVelocity *
                (Math.cos(newRobotDirection) - Math.cos(robot.getDirection()));
        if (!Double.isFinite(newRobotYCoordinate)) {
            newRobotYCoordinate = robot.getYCoordinate() + ROBOT_VELOCITY * TIMER_UPDATE_PERIOD * Math.sin(robot.getDirection());
        }
        robot.setXCoordinate(newRobotXCoordinate);
        robot.setYCoordinate(newRobotYCoordinate);
        robot.setDirection(asNormalizedRadians(newRobotDirection));
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

    private int round(double value) {
        return (int) (value + 0.5);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, robot.getDirection());
        drawTarget(g2d, target.getXCoordinate(), target.getYCoordinate());
    }

    private void fillOval(Graphics g, int centerXCoordinate, int centerYCoordinate, int firstDiameter, int secondDiameter) {
        g.fillOval(centerXCoordinate - firstDiameter / 2, centerYCoordinate - secondDiameter / 2, firstDiameter, secondDiameter);
    }

    private void drawOval(Graphics g, int centerXCoordinate, int centerYCoordinate, int firstDiameter, int secondDiameter) {
        g.drawOval(centerXCoordinate - firstDiameter / 2, centerYCoordinate - secondDiameter / 2, firstDiameter, secondDiameter);
    }

    private void drawRobot(Graphics2D g, double direction) {
        int robotCenterX = round(robot.getXCoordinate());
        int robotCenterY = round(robot.getYCoordinate());
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, ROBOT_BODY_FIRST_DIAMETER, ROBOT_BODY_SECOND_DIAMETER);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, ROBOT_BODY_FIRST_DIAMETER, ROBOT_BODY_SECOND_DIAMETER);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + ROBOT_HEAD_X_OFFSET, robotCenterY, ROBOT_HEAD_DIAMETER, ROBOT_HEAD_DIAMETER);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + ROBOT_HEAD_X_OFFSET, robotCenterY, ROBOT_HEAD_DIAMETER, ROBOT_HEAD_DIAMETER);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(TARGET_THETA, TARGET_ANCHORX, TARGET_ANCHORY);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, TARGET_DIAMETER, TARGET_DIAMETER);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, TARGET_DIAMETER, TARGET_DIAMETER);
    }
}
