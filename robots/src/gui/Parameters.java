package gui;

import log.Logger;

import java.awt.*;
import java.util.Observable;

public class Parameters extends Observable {
    public double m_robotPositionX = 100;
    public double m_robotPositionY = 100;
    public double m_robotDirection = 0;

    public int m_targetPositionX = 150;
    public int m_targetPositionY = 100;

    public static final double maxVelocity = 0.1;
    public static final double maxAngularVelocity = 0.001;


    public void updateTarget(Point point) {
        m_targetPositionX = point.x;
        m_targetPositionY = point.y;
        Logger.debug("Переместили цель");
        setChanged();
        notifyObservers();
    }

    public void updateRobot(double pX, double pY, double dir) {
        m_robotPositionX = pX;
        m_robotPositionY = pY;
        m_robotDirection = dir;
        //Logger.debug("Новые координаты: "+ pX + " " + pY);
        setChanged();
        notifyObservers();
    }
}
