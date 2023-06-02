package gui;

import log.Logger;

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
        var newX = parameters.m_robotPositionX;
        var newY = parameters.m_robotPositionY;
        var koef = 0.4;

        if (parameters.m_robotPositionX < parameters.m_targetPositionX)
            newX += koef;
        if (parameters.m_robotPositionY < parameters.m_targetPositionY)
            newY += koef;
        if (parameters.m_robotPositionX > parameters.m_targetPositionX)
            newX -= koef;
        if (parameters.m_robotPositionY > parameters.m_targetPositionY)
            newY -= koef;
        var dir = angleTo(newX, newY, parameters.m_targetPositionX, parameters.m_targetPositionY);
        parameters.updateRobot(newX, newY, dir);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
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


}
