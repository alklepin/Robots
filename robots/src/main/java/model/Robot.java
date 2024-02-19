package main.java.model;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.Observable;

public class Robot extends Observable implements Entity {
    public static final String ROBOT_POSITION_CHANGED = "The robot's position has changed";
    public static final double MAX_VELOCITY = 0.1;
    public static final double MAX_ANGULAR_VELOCITY = 0.01;
    public static final int INITIAL_SATIETY = 50;
    public static final int MAX_SATIETY = 100;
    private double positionX;
    private double positionY;
    private Target target;
    private Dimension dimension;
    private volatile double robotDirection;
    private int satiety;
    private TypeRobot type;

    public Robot() {
        this(300.0, 300.0);
    }

    public Robot(double x, double y) {
        this.positionX = x;
        this.positionY = y;
        this.target = new Target();
        this.robotDirection = Math.random() * 10.0;
        this.dimension = new Dimension(300, 300);
        this.satiety = (int)(50.0 + Math.random() * 50.0);
        this.type = getType(this.satiety);
    }

    public void setType(TypeRobot type) {
        this.type = type;
    }

    public TypeRobot getType() {
        return this.type;
    }

    public void changeSatiety(int satiety) {
        this.satiety += satiety;
        this.type = getType(this.satiety);
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
        if (!this.target.isPositionCorrect(dimension)) {
            this.target = new Target((int)(Math.random() * (double)dimension.width), (int)(Math.random() * (double)dimension.height));
        }

    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public double getPositionX() {
        return this.positionX;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public double getPositionY() {
        return this.positionY;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public double getRobotDirection() {
        return this.robotDirection;
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
        while(angle < 0.0) {
            angle += 6.283185307179586;
        }

        while(angle >= 6.283185307179586) {
            angle -= 6.283185307179586;
        }

        return angle;
    }

    private double normalizedPositionX(double x) {
        if (x < 0.0) {
            return 0.0;
        } else {
            return x > (double)this.dimension.width ? (double)this.dimension.width : x;
        }
    }

    private double normalizedPositionY(double y) {
        if (y < 0.0) {
            return 0.0;
        } else {
            return y > (double)this.dimension.height ? (double)this.dimension.height : y;
        }
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

    public Target getTarget() {
        return this.target;
    }

    public void setTarget(Point point) {
        this.target.setTargetPosition(point);
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0.0, 0.1);
        angularVelocity = applyLimits(angularVelocity, -0.01, 0.01);
        double newX = this.getPositionX() + velocity / angularVelocity * (Math.sin(this.getRobotDirection() + angularVelocity * duration) - Math.sin(this.getRobotDirection()));
        if (!Double.isFinite(newX)) {
            newX = this.getPositionX() + velocity * duration * Math.cos(this.getRobotDirection());
        }

        double newY = this.getPositionY() - velocity / angularVelocity * (Math.cos(this.getRobotDirection() + angularVelocity * duration) - Math.cos(this.getRobotDirection()));
        if (!Double.isFinite(newY)) {
            newY = this.getPositionY() + velocity * duration * Math.sin(this.getRobotDirection());
        }

        this.setPositionX(this.normalizedPositionX(newX));
        this.setPositionY(this.normalizedPositionY(newY));
        double newDirection = asNormalizedRadians(this.getRobotDirection() + angularVelocity * duration);
        this.setRobotDirection(newDirection);
        this.setChanged();
        this.notifyObservers("The robot's position has changed");
    }

    public void update() {
        if (this.type != TypeRobot.DEAD) {
            if (this.interactionWithEnvironment()) {
                this.onTargetAchieved();
            }

            double angleToTarget = angleTo(this.getPositionX(), this.getPositionY(), (double)this.target.getX(), (double)this.target.getY());
            double angularVelocity = 0.0;
            if (angleToTarget > this.getRobotDirection()) {
                angularVelocity = 0.01;
            }

            if (angleToTarget < this.getRobotDirection()) {
                angularVelocity = -0.01;
            }

            this.moveRobot(0.1, angularVelocity, 10.0);
        }
    }

    private boolean interactionWithEnvironment() {
        double distance = distance((double)this.target.getX(), (double)this.target.getY(), this.getPositionX(), this.getPositionY());
        return distance < 0.5;
    }

    public void onStart(PropertyChangeSupport publisher) {
        publisher.addPropertyChangeListener(this);
    }

    private void onTargetAchieved() {
        this.setTarget(new Point((int)(Math.random() * (double)this.dimension.width), (int)(Math.random() * (double)this.dimension.height)));
        this.satiety += 25;
        this.type = getType(this.satiety);
    }

    private static TypeRobot getType(int satiety) {
        if (satiety > 50) {
            return TypeRobot.CALM;
        } else {
            return satiety <= 0 ? TypeRobot.DEAD : TypeRobot.HUNGRY;
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("new point")) {
            this.setTarget((Point)evt.getNewValue());
        }

        if (evt.getPropertyName().equals("change satiety")) {
            this.changeSatiety((Integer)evt.getNewValue());
        }

        if (evt.getPropertyName().equals("set dimension")) {
            this.setDimension((Dimension)evt.getNewValue());
        }

    }
}