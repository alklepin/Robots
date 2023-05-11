package models;


import models.states.TargetState;
import models.states.TargetStateReader;

import java.util.Observable;

public class TargetModel extends Observable {
    private volatile double m_PositionX = 100;
    private volatile double m_PositionY = 100;

    public TargetModel(double posX, double posY) {
        setPos(posX, posY);
    }


    public TargetStateReader getState(){
        TargetStateReader ans;
        synchronized (this){
            ans=new TargetState(m_PositionX,m_PositionY);
        }
        return ans;
    }

    public void setPos(double posX, double posY) {
        m_PositionX = posX;
        m_PositionY = posY;
        setChanged();
        notifyObservers();
    }
}
