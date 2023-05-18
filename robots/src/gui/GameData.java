package gui;

public class GameData {

    volatile double m_robotPositionX = 100;
    volatile double m_robotPositionY = 100;
    volatile double m_robotDirection = 0;

    volatile int m_targetPositionX = 150;
    volatile int m_targetPositionY = 100;

    static final double maxVelocity = 0.1;
    static final double maxAngularVelocity = 0.001;

}
