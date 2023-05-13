package ru.kemichi.robots.models;

import ru.kemichi.robots.utility.DoublePoint;

import java.awt.*;
import java.util.Observable;

public class Robot extends Observable {
    private final DoublePoint robotPosition = new DoublePoint(100, 100);
    private volatile double robotDirection = 0;
    private double angle = 0;


    private volatile Point targetPosition = new Point(150, 100);

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public DoublePoint getRobotPosition() {
        return robotPosition;
    }

    public double getRobotDirection() {
        return robotDirection;
    }

    public Point getTargetPosition() {
        return targetPosition;
    }

    private static double distance(Point target, DoublePoint robot) {
        double diffX = target.getX() - robot.getX();
        double diffY = target.getY() - robot.getY();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(Point target, DoublePoint robot) {
        double diffX = target.getX() - robot.getX();
        double diffY = target.getY() - robot.getY();

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public void setTargetPosition(Point p) {
        targetPosition = p;
        setChanged();
        notifyObservers();
        clearChanged();
    }

    private double calculateAngularVelocity(double angle) {
        double diff = robotDirection - angle;
        if (robotDirection >= Math.PI) {
            if (diff < Math.PI & angle < robotDirection) {
                return -maxAngularVelocity;
            }
            return maxAngularVelocity;
            }
        else if (10e-7 >= Math.abs(diff)) {
            return 0;
        }
        else {
            if (robotDirection < angle & diff > -Math.PI) {
                return maxAngularVelocity;
            }
            return -maxAngularVelocity;
        }
    }

    public void onModelUpdateEvent(Dimension windowDimension) {
        double distance = distance(targetPosition, robotPosition);
        if (distance < 0.5) {
            setChanged();
            notifyObservers();
            clearChanged();
            return;
        }
        double angleToTarget = angleTo(targetPosition, robotPosition);
        angle = asNormalizedRadians(angleToTarget - robotDirection);
        double angularVelocity = calculateAngularVelocity(angleToTarget);
        if (angleToTarget > robotDirection) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < robotDirection) {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(maxVelocity, angularVelocity, windowDimension);
    }

    private static double applyLimits(double value, double max) {
        if (value < 0)
            return 0;
        return Math.min(value, max);
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
    private void moveRobot(double velocity, double angularVelocity, Dimension windowDimension) {
        velocity = applyLimits(velocity, maxVelocity);
        robotDirection = asNormalizedRadians(robotDirection + Math.min(angularVelocity, angle) * 10);
        robotPosition.setX(robotPosition.getX() + velocity * 10 * Math.cos(robotDirection));
        robotPosition.setY(robotPosition.getY() + velocity * 10 * Math.sin(robotDirection));

        if (windowDimension.width != 0) {
            robotPosition.setX(applyLimits(robotPosition.getX(), windowDimension.width));
        }
        if (windowDimension.height != 0) {
            robotPosition.setY(applyLimits(robotPosition.getY(), windowDimension.height));
        }

        setChanged();
        notifyObservers();
        clearChanged();
    }
}