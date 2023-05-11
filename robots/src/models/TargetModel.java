package models;

import java.util.Observable;

public class TargetModel extends Observable {
    private volatile double m_PositionX = 100;
    private volatile double m_PositionY = 100;

    public TargetModel(double posX, double posY) {
        setPos(posX, posY);
    }


    public ModelCoordinate getPos(){
        ModelCoordinate ans=new ModelCoordinate(0,0);
        synchronized (this){
            ans.x=m_PositionX;
            ans.y=m_PositionY;
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
