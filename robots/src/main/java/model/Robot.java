package main.java.model;

import main.java.gui.GameVisualizer;
import model.TypeRobot;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class Robot implements Entity {
    public static final Object ROBOT_POSITION_CHANGED = "robot position changed";
    private double positionX;
    private double positionY;
    private Target target;
    private Dimension dimension;
    private volatile double robotDirection;
    public static final double maxVelocity = 0.1;
    public static final double maxAngularVelocity = 0.01;
    private static final int INITIAL_SATIETY = 50;
    private static final int MAX_SATIETY = 100;
    private int satiety;
    private model.TypeRobot type;
    private boolean isAlive;
    private boolean isTargetAchieved;
    private Point targetPosition;

    public Robot(double x, double y) {
        this.positionX = x;
        this.positionY = y;
        this.target = new Target();
        this.robotDirection = Math.random() * 10;;
        this.dimension = new Dimension(300, 300);
        this.satiety = (int) (INITIAL_SATIETY + Math.random() * (MAX_SATIETY - INITIAL_SATIETY));
        this.isAlive = true;
        this.type= model.TypeRobot.randomType();
    }

    public Robot() {
        this.positionX = 300;
        this.positionY = 300;
        this.target = new Target();
        this.robotDirection = 0;
    }

    private void setRandomType() {
        this.type = model.TypeRobot.randomType();
    }

    public void setType(model.TypeRobot type) {
        this.type = type;
    }
    public model.TypeRobot getType() {
        return type;
    }

    public int getSatiety() {
        return satiety;
    }

//    public TypeRobot getType() {
//        if (satiety >= MAX_SATIETY) {
//            return TypeRobot.FAT;
//        } else if (satiety >= MEDIUM_SATIETY) {
//            return TypeRobot.NORMAL;
//        } else {
//            return TypeRobot.HUNGRY;
//        }
//    }

    public void changeSatiety(int satiety) {
        this.satiety += satiety;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
        if (!target.isPositionCorrect(dimension)) {
            target = new Target((int) (Math.random() * dimension.width), (int) (Math.random() * dimension.height));
        }
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public double getPositionX() {
        return positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getRobotDirection() {
        return robotDirection;
    }

    public void setRobotDirection(double robotDirection) {
        this.robotDirection = robotDirection;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private double normalizedPositionX(double x) {
        if (x < 0)
            return 0;
        if (x > dimension.width)
            return dimension.width;
        return x;
    }

    private double normalizedPositionY(double y) {
        if (y < 0)
            return 0;
        if (y > dimension.height)
            return dimension.height;
        return y;
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Point point) {
        this.target.setTargetPosition(point);
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, Robot.maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -Robot.maxAngularVelocity, Robot.maxAngularVelocity);
        double newX = getPositionX() + velocity / angularVelocity *
                (Math.sin(getRobotDirection() + angularVelocity * duration) -
                        Math.sin(getRobotDirection()));
        if (!Double.isFinite(newX)) {
            newX = getPositionX() + velocity * duration * Math.cos(getRobotDirection());
        }
        double newY = getPositionY() - velocity / angularVelocity *
                (Math.cos(getRobotDirection() + angularVelocity * duration) -
                        Math.cos(getRobotDirection()));
        if (!Double.isFinite(newY)) {
            newY = getPositionY() + velocity * duration * Math.sin(getRobotDirection());
        }
        setPositionX(normalizedPositionX(newX));
        setPositionY(normalizedPositionY(newY));
        double newDirection = asNormalizedRadians(getRobotDirection() + angularVelocity * duration);
        setRobotDirection(newDirection);
    }

    @Override
    public void update() {
        updateRobotState();
        if (needSkipUpdate()) {
            return;
        }
        interactionWithEnvironment();
        transformRobot();
    }

    private void updateRobotState() {
        if (!this.isAlive) {
            this.setType(TypeRobot.DEAD);
        }
        if (this.satiety < 50) {
            if (this.satiety < 0) {
                isAlive = false;
            }
            this.setType(TypeRobot.HUNGRY);
        }
    }

    private boolean needSkipUpdate() {
        return !this.isAlive || this.isTargetAchieved;
    }

    private void interactionWithEnvironment() {
        double distance = distance(target.getX(), target.getY(),
                getPositionX(), getPositionY());
        this.isTargetAchieved = false;
        if (distance < 0.5) {
            this.isTargetAchieved = true;
            this.onTargetAchieved();
        }
    }

    private void transformRobot() {
        double angleToTarget = angleTo(getPositionX(), getPositionY(),
                target.getX(), target.getY());
        double angularVelocity = 0;
        if (angleToTarget > getRobotDirection()) {
            angularVelocity = Robot.maxAngularVelocity;
        }
        if (angleToTarget < getRobotDirection()) {
            angularVelocity = -Robot.maxAngularVelocity;
        }

        moveRobot(Robot.maxVelocity, angularVelocity, 10);
    }

    @Override
    public void onStart(PropertyChangeSupport publisher) {
        publisher.addPropertyChangeListener(this);
    }

    @Override
    public void onFinish(PropertyChangeSupport publisher) {
        publisher.removePropertyChangeListener(this);
    }

    private void onTargetAchieved() {
        this.setTarget(new Point((int) (Math.random() * dimension.width), (int) (Math.random() * dimension.height)));
        this.satiety += 25;
        if (this.satiety > 50) {
            this.setType(TypeRobot.CALM);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("new point"))
            setTarget((Point) evt.getNewValue());
        if (evt.getPropertyName().equals("change satiety"))
            changeSatiety((int) evt.getNewValue());
        if (evt.getPropertyName().equals("set dimension"))
            setDimension((Dimension) evt.getNewValue());
    }

    public void setTargetPosition(Point targetPosition) {
        this.targetPosition = targetPosition;
    }

    private final ArrayList<GameVisualizer> m_PositionWindowObservers = new ArrayList<GameVisualizer>();

    public void addObserver(GameVisualizer observer) {
        m_PositionWindowObservers.add(observer);
    }
}
