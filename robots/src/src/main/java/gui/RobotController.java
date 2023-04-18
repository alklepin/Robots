package gui;

import java.awt.*;

public class RobotController
{
    public RobotController (RobotModel robotModel, RobotView robotView)
    {
        this.robotModel = robotModel;
        this.robotView = robotView;
    }

    private RobotModel robotModel;
    private RobotView robotView;

    public double getPositionX()
    {
        return robotModel.getPositionX();
    }
    public void setPositionX(double positionX)
    {
        robotModel.setPositionX(positionX);
    }

    public double getPositionY()
    {
        return robotModel.getPositionY();
    }
    public void setPositionY(double positionY)
    {
        robotModel.setPositionY(positionY);
    }

    public double getDirection()
    {
        return robotModel.getDirection();
    }
    public void setDirection(double direction)
    {
        robotModel.setDirection(direction);
    }

    public void move(double velocity, double angularVelocity, double duration)
    {
        robotModel.move(velocity, angularVelocity, duration);
    }

    public void draw(Graphics2D g)
    {
        robotView.draw(g, robotModel);
    }

}