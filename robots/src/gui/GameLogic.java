package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameLogic {
    private final GameData gameData = new GameData();
    private ArrayList<WindowWithPathState> observers;
    private final Timer m_timer = initTimer();

    private Boolean ifGameStarted;

    private static Timer initTimer()
    {
        Timer timer = new Timer("notifies generator", true);
        return timer;
    }

    GameLogic() {
        observers = new ArrayList<>();
        ifGameStarted = false;
    }

    protected void setTargetPosition(Point p)
    {
        gameData.m_targetPositionX = p.x;
        gameData.m_targetPositionY = p.y;
        if (!ifGameStarted){
            ifGameStarted = true;
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                notifyObservers();
            }
        }, 0, 1000);
    }
    }

    protected Point getTargetPosition()
    {
        return new Point(gameData.m_targetPositionX, gameData.m_targetPositionY);
    }
    protected Point getRobotPosition()
    {
        return new Point((int) gameData.m_robotPositionX, (int) gameData.m_robotPositionY);
    }

    protected double getRobotDirection()
    {
        return gameData.m_robotDirection;
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

    private static double asNormalizedRadians(double angle)
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

    protected void onModelUpdateEvent()
    {

        double distance = distance(gameData.m_targetPositionX, gameData.m_targetPositionY,
                gameData.m_robotPositionX, gameData.m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = gameData.maxVelocity;
        double angleToTarget = angleTo(gameData.m_robotPositionX, gameData.m_robotPositionY, gameData.m_targetPositionX, gameData.m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > gameData.m_robotDirection)
        {
            angularVelocity = gameData.maxAngularVelocity;
        }
        if (angleToTarget < gameData.m_robotDirection)
        {
            angularVelocity = -gameData.maxAngularVelocity;
        }

        moveRobot(velocity, angularVelocity, 10);

    }

    private void notifyObservers(){
        for (WindowWithPathState window : this.observers) {
            Point p = new Point((int) gameData.m_robotPositionX, (int) gameData.m_robotPositionY);
            window.update(p);
        }
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, gameData.maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -gameData.maxAngularVelocity, gameData.maxAngularVelocity);
        double newX = gameData.m_robotPositionX + velocity / angularVelocity *
                (Math.sin(gameData.m_robotDirection  + angularVelocity * duration) -
                        Math.sin(gameData.m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = gameData.m_robotPositionX + velocity * duration * Math.cos(gameData.m_robotDirection);
        }
        double newY = gameData.m_robotPositionY - velocity / angularVelocity *
                (Math.cos(gameData.m_robotDirection  + angularVelocity * duration) -
                        Math.cos(gameData.m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = gameData.m_robotPositionY + velocity * duration * Math.sin(gameData.m_robotDirection);
        }
        gameData.m_robotPositionX = newX;
        gameData.m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(gameData.m_robotDirection + angularVelocity * duration);
        gameData.m_robotDirection = newDirection;
    }

    public void addObserver(WindowWithPathState window) {
        this.observers.add(window);
    }

    public void removeObserver(WindowWithPathState window) {
        this.observers.remove(window);
    }

}
