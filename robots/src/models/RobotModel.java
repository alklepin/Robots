package models;


import models.states.RobotState;
import models.states.RobotStateReader;
import models.states.TargetStateReader;

import static utils.MathUtils.*;
import java.util.Observable;

public class RobotModel extends Observable {


    private volatile double m_PositionX = 100;
    private volatile double m_PositionY = 100;
    private TargetModel m_Target;

    private volatile double m_Direction = 0;


    private double turn_duration=10;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;


    public RobotModel(double m_PositionX, double m_PositionY, double m_Direction,TargetModel target) {
        this.m_PositionX = m_PositionX;
        this.m_PositionY = m_PositionY;
        this.m_Direction = m_Direction;
        m_Target=target;
    }
    public RobotModel(RobotState state,TargetModel target){
        this.m_Target=target;
        this.m_PositionY=state.getY();
        this.m_PositionX=state.getX();
        this.m_Direction=state.getDir();
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



    public RobotState getState(){
        RobotState ans;
        synchronized (this){
            ans=new RobotState(m_PositionX,m_PositionY,m_Direction);
        }
        return ans;

    }


    private double calculateVelocity(){
        return maxVelocity;
    }
    private double calculateAngularVelocity(){
        TargetStateReader targetCoord= m_Target.getState();
        double angleToTarget = angleTo(m_PositionX, m_PositionY, targetCoord.getX(), targetCoord.getY());
        double angularVelocity = 0;
        if (angleToTarget > m_Direction)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_Direction)
        {
            angularVelocity = -maxAngularVelocity;
        }
        if(Math.abs(angleToTarget-m_Direction)>Math.PI){
            angularVelocity=-angularVelocity;

        }
        return angularVelocity;
    }
    private boolean isTooClose(){
        TargetStateReader targetCoord= m_Target.getState();
        double distance = distance(targetCoord.getX(), targetCoord.getY(),
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
