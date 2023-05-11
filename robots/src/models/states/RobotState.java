package models.states;

public class RobotState implements RobotStateReader{


    private volatile double m_x;
    private volatile double m_y;
    private volatile double m_dir;

    public RobotState(double m_x, double m_y, double m_dir) {
        this.m_x = m_x;
        this.m_y = m_y;
        this.m_dir = m_dir;
    }

    @Override
    public double getX() {
        return m_x;
    }

    @Override
    public double getY() {
        return m_y;
    }

    @Override
    public double getDir() {
        return m_dir;
    }


}
