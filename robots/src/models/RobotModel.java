package models;

import java.awt.*;
import java.util.Observable;

public class RobotModel extends Observable {


    private volatile double m_PositionX = 100;
    private volatile double m_PositionY = 100;
    private volatile double m_Direction = 0;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;
    private double turn_duration=10;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;


    public RobotModel(double m_PositionX, double m_PositionY, double m_Direction,int targetPosX,int targetPosY) {
        this.m_PositionX = m_PositionX;
        this.m_PositionY = m_PositionY;
        this.m_Direction = m_Direction;
        this.m_targetPositionX=targetPosX;
        this.m_targetPositionY=targetPosY;
    }

    public void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }
    public void updatePos(){
        if(isTooClose()){
            return;
        }
        double velocity=calculateVelocity();
        double angular = calculateAngularVelocity();
        move(velocity,angular);
        setChanged();
        notifyObservers();
    }

    public double getM_PositionX() {
        return m_PositionX;
    }

    public double getM_PositionY() {
        return m_PositionY;
    }

    public double getM_Direction() {
        return m_Direction;
    }
    static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }
    private double calculateVelocity(){
        return maxVelocity;
    }
    private double calculateAngularVelocity(){
        double angleToTarget = angleTo(m_PositionX, m_PositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_Direction)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_Direction)
        {
            angularVelocity = -maxAngularVelocity;
        }
        return angularVelocity;
    }
    private boolean isTooClose(){
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_PositionX, m_PositionY);
        return distance<0.5;
    }
    private void move(double velocity,double angularVelocity){
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_PositionX + velocity / angularVelocity *
                (Math.sin(m_Direction  + angularVelocity * turn_duration) -
                        Math.sin(m_Direction));
        if (!Double.isFinite(newX))
        {
            newX = m_PositionX + velocity * turn_duration * Math.cos(m_Direction);
        }
        double newY = m_PositionY - velocity / angularVelocity *
                (Math.cos(m_Direction  + angularVelocity * turn_duration) -
                        Math.cos(m_Direction));
        if (!Double.isFinite(newY))
        {
            newY = m_PositionY + velocity * turn_duration * Math.sin(m_Direction);
        }
        m_PositionX = newX;
        m_PositionY = newY;
        double newDirection = asNormalizedRadians(m_Direction + angularVelocity * turn_duration);
        m_Direction = newDirection;
    }






}
