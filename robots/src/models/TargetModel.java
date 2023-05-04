package models;

public class TargetModel {
    private volatile double m_PositionX = 100;
    private volatile double m_PositionY = 100;

    public TargetModel(double posX, double posY) {
        setPos(posX, posY);
    }

    public double getPosX() {
        return m_PositionX;
    }

    public double getPosY() {
        return m_PositionY;
    }

    public void setPos(double posX, double posY) {
        m_PositionX = posX;
        m_PositionY = posY;
    }
}
