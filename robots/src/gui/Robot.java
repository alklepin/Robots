package gui;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static gui.Constants.GameVisualizerConstants.*;
import static gui.Constants.GameVisualizerConstants.ROBOT_HEAD_DIAMETER;

public class Robot extends GameModel implements Moveable {

    private volatile double direction;
    private double angularVelocity;

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getDirection() {
        return direction;
    }

    public Robot(double xCoordinate, double yCoordinate, double direction) {
        super(xCoordinate, yCoordinate);
        this.direction = direction;
    }

    @Override
    public void draw(Graphics2D g) {
        int robotCenterX = MathModule.round(getxCoordinate());
        int robotCenterY = MathModule.round(getyCoordinate());
        AffineTransform t = AffineTransform.getRotateInstance(getDirection(), robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        GameVisualizer.fillOval(g, robotCenterX, robotCenterY, ROBOT_BODY_FIRST_DIAMETER, ROBOT_BODY_SECOND_DIAMETER);
        g.setColor(Color.BLACK);
        GameVisualizer.drawOval(g, robotCenterX, robotCenterY, ROBOT_BODY_FIRST_DIAMETER, ROBOT_BODY_SECOND_DIAMETER);
        g.setColor(Color.WHITE);
        GameVisualizer.fillOval(g, robotCenterX + ROBOT_HEAD_X_OFFSET, robotCenterY, ROBOT_HEAD_DIAMETER, ROBOT_HEAD_DIAMETER);
        g.setColor(Color.BLACK);
        GameVisualizer.drawOval(g, robotCenterX + ROBOT_HEAD_X_OFFSET, robotCenterY, ROBOT_HEAD_DIAMETER, ROBOT_HEAD_DIAMETER);
    }

    @Override
    public void move() {
        double newRobotDirection = getDirection() + getAngularVelocity() * TIMER_UPDATE_PERIOD;
        double newRobotXCoordinate = getxCoordinate() + ROBOT_VELOCITY * TIMER_UPDATE_PERIOD * Math.cos(getDirection());
        double newRobotYCoordinate = getyCoordinate() + ROBOT_VELOCITY * TIMER_UPDATE_PERIOD * Math.sin(getDirection());
        setxCoordinate(newRobotXCoordinate);
        setyCoordinate(newRobotYCoordinate);
        setDirection(MathModule.asNormalizedRadians(newRobotDirection));
    }
}
