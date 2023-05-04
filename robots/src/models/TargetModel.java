package models;

public class TargetModel {
    private volatile double m_PositionX = 100;
    private volatile double m_PositionY = 100;

    public TargetModel(double posX, double posY) {
        setPos(posX, posY);
    }


    public CoordPair getPos(){
        CoordPair ans=new CoordPair(0,0);
        synchronized (this){
            ans.x=m_PositionX;
            ans.y=m_PositionY;
        }
        return ans;
    }

    public void setPos(double posX, double posY) {
        m_PositionX = posX;
        m_PositionY = posY;
    }
}
