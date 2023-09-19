package application.model;


import application.model.additionals.Mood;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class Bacteria implements Entity
{
    private double positionX;
    private double positionY;
    private volatile double bacteriaDirection;
    private Target target;

    private Mood mood;
    private int satiety;
    public static double maxVelocity = 0.05;
    public static final double maxAngularVelocity = 0.005;
    private boolean isAlive;
    private Dimension dimension;
    private static final int INITIAL_SATIETY = 50;
    private static final int MAX_SATIETY = 100;

    public Bacteria(double x, double y)
    {
        this.positionX = x;
        this.positionY = y;
        this.target = new Target();
        this.bacteriaDirection = Math.random() * 10;
        this.dimension = new Dimension(400, 400);
        this.mood = Mood.randomMood();
        this.satiety = (int) (INITIAL_SATIETY + Math.random() * (MAX_SATIETY - INITIAL_SATIETY));
        this.isAlive = true;
    }

    private void setRandomMood()
    {
        this.mood = Mood.randomMood();
    }

    public void setMood(Mood mood)
    {
        this.mood = mood;
    }

    public void setDimension(Dimension dimension)
    {
        this.dimension = dimension;
        if (!target.isPositionCorrect(dimension))
        {
            target = new Target((int) (Math.random() * dimension.width), (int) (Math.random() * dimension.height));
        }
    }

    public Dimension getDimension()
    {
        return this.dimension;
    }

    public double getPositionX()
    {
        return positionX;
    }

    public void setPositionX(double positionX)
    {
        this.positionX = positionX;
    }

    public double getPositionY()
    {
        return positionY;
    }

    public void setPositionY(double positionY)
    {
        this.positionY = positionY;
    }

    public double getBacteriaDirection()
    {
        return bacteriaDirection;
    }

    public void setBacteriaDirection(double bacteriaDirection)
    {
        this.bacteriaDirection = bacteriaDirection;
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
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private double normalizedPositionX(double x)
    {
        if (x < 0)
            return 0;
        if (x > dimension.width)
            return dimension.width;
        return x;
    }

    private double normalizedPositionY(double y)
    {
        if (y < 0)
            return 0;
        if (y > dimension.height)
            return dimension.height;
        return y;
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        return Math.min(value, max);
    }

    public Target getTarget()
    {
        return target;
    }

    public void changeSatiety(int satiety)
    {
        this.satiety += satiety;
    }

    public void setTarget(Point point)
    {
        if (point.x > dimension.width || point.y > dimension.height)
        {
            this.target.setTargetPosition(new Point(point.x / dimension.width, point.y / dimension.height));
        }
        this.target.setTargetPosition(point);
    }

    private void moveBacteria(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, Bacteria.maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -Bacteria.maxAngularVelocity, Bacteria.maxAngularVelocity);
        double newX = getPositionX() + velocity / angularVelocity *
                (Math.sin(getBacteriaDirection() + angularVelocity * duration) -
                        Math.sin(getBacteriaDirection()));
        if (!Double.isFinite(newX))
            newX = getPositionX() + velocity * duration * Math.cos(getBacteriaDirection());

        double newY = getPositionY() - velocity / angularVelocity *
                (Math.cos(getBacteriaDirection() + angularVelocity * duration) -
                        Math.cos(getBacteriaDirection()));
        if (!Double.isFinite(newY))
            newY = getPositionY() + velocity * duration * Math.sin(getBacteriaDirection());

        setPositionX(normalizedPositionX(newX));
        setPositionY(normalizedPositionY(newY));
        double newDirection = asNormalizedRadians(getBacteriaDirection() + angularVelocity * duration);
        setBacteriaDirection(newDirection);
    }


    @Override
    public void update()
    {
        if (!this.isAlive)
        {
            this.setMood(Mood.DEAD);

            return;
        }
        if (this.satiety < 50)
        {
            if (this.satiety < 0)
                isAlive = false;
            this.setMood(Mood.HUNGRY);

        }
        double distance = distance(target.getX(), target.getY(),
                getPositionX(), getPositionY());

        if (distance < 0.5)
        {
            this.onTargetAchieved();
            return;
        }
        double angleToTarget = angleTo(getPositionX(), getPositionY(),
                target.getX(), target.getY());
        double angularVelocity = 0;
        if (angleToTarget > getBacteriaDirection())
            angularVelocity = Bacteria.maxAngularVelocity;
        if (angleToTarget < getBacteriaDirection())
            angularVelocity = -Bacteria.maxAngularVelocity;

        moveBacteria(Bacteria.maxVelocity, angularVelocity, 10);
    }

    @Override
    public void onStart(PropertyChangeSupport publisher)
    {
        publisher.addPropertyChangeListener(this);
    }

    @Override
    public void onFinish(PropertyChangeSupport publisher)
    {
        publisher.removePropertyChangeListener(this);
    }

    @Override
    public boolean isAlive()
    {
        return isAlive;
    }

    private void onTargetAchieved()
    {
        this.setTarget(new Point((int) (Math.random() * dimension.width), (int) (Math.random() * dimension.height)));
        this.satiety += 20;
        if (this.satiety > 50)
        {
            this.setRandomMood();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName().equals("new point"))
            setTarget((Point) evt.getNewValue());
        if (evt.getPropertyName().equals("change satiety"))
            changeSatiety((int) evt.getNewValue());
        if (evt.getPropertyName().equals("set dimension"))
            setDimension((Dimension) evt.getNewValue());
    }

    public Mood getMood()
    {
        return mood;
    }
}