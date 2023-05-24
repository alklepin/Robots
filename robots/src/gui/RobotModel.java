package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

import static gui.Parameters.*;
import static java.awt.geom.Point2D.distance;

public class RobotModel extends JPanel {

    private final Timer m_timer = initTimer();
    public Parameters parameters;

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public RobotModel(Parameters parameters) {
        this.parameters = parameters;
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 10);


    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

    }


    protected void onModelUpdateEvent() {
        double distance = distance(parameters.m_targetPositionX, parameters.m_targetPositionY,
                parameters.m_robotPositionX, parameters.m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        parameters.m_robotDirection = asNormalizedRadians(parameters.m_robotDirection);
        double velocity = maxVelocity;
        double angleToTarget = angleTo(parameters.m_robotPositionX, parameters.m_robotPositionY, parameters.m_targetPositionX, parameters.m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > parameters.m_robotDirection) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < parameters.m_robotDirection) {
            angularVelocity = -maxAngularVelocity;
        }


        moveRobot(velocity, angularVelocity, 10);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = parameters.m_robotPositionX + velocity / angularVelocity *
                (Math.sin(parameters.m_robotDirection + angularVelocity * duration) -
                        Math.sin(parameters.m_robotDirection));
        if (!Double.isFinite(newX)) {
            newX = parameters.m_robotPositionX + velocity * duration * Math.cos(parameters.m_robotDirection);
        }
        double newY = parameters.m_robotPositionY - velocity / angularVelocity *
                (Math.cos(parameters.m_robotDirection + angularVelocity * duration) -
                        Math.cos(parameters.m_robotDirection));
        if (!Double.isFinite(newY)) {
            newY = parameters.m_robotPositionY + velocity * duration * Math.sin(parameters.m_robotDirection);
        }
        double newDirection = asNormalizedRadians(parameters.m_robotDirection + angularVelocity * duration);
        parameters.updateRobot(newX, newY, newDirection);
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

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }


}
