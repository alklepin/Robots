package robots.domain.game;

import robots.domain.DoublePoint;
import robots.domain.Point;
import robots.domain.events.Event;
import robots.interfaces.GameObject;
import robots.gui.Visualization;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;


public class Robot extends Observable implements GameObject, Observer {
    private DoublePoint robotPosition;
    private DoublePoint targetPosition;
    private final double maxVelocity = 1;
    private final double maxAngularVelocity = 0.01;
    private long lastPositionUpdate;

    private volatile double robotDirection = 0;

    public Robot(double x, double y) {
        robotPosition = new DoublePoint(x, y);
        this.targetPosition = robotPosition;
        lastPositionUpdate = System.currentTimeMillis();
    }

    public double getRobotDirection() {
        return robotDirection;
    }

    private static double distance(DoublePoint target, DoublePoint robot) {
        double diffX = target.x() - robot.x();
        double diffY = target.y() - robot.y();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(DoublePoint target, DoublePoint robot) {
        double diffX = target.x() - robot.x();
        double diffY = target.y() - robot.y();

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    private void setTargetPosition(DoublePoint p) {
        targetPosition = p;
    }

    private static double calculateAngularVelocity(double angle, double robotDirection, double maxAngularVelocity) {
        double diff = angle - robotDirection;
        diff = asNormalizedRadians(diff);

        return Math.signum(diff) * Math.min(maxAngularVelocity, Math.abs(diff));
    }

    private static double applyLimits(double value, double max) {
        if (value < 0)
            return 0;
        return Math.min(value, max);
    }

    private static double asNormalizedRadians(double angle) {
        angle = angle % (2 * Math.PI);
        if (angle < -Math.PI) {
            angle += 2 * Math.PI;
        } else if (angle > Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private void moveRobot(double velocity, double angularVelocity) {
        velocity = applyLimits(velocity, maxVelocity);
        robotDirection = asNormalizedRadians(robotDirection + angularVelocity);

        robotPosition = new DoublePoint(
                robotPosition.x() + velocity * Math.cos(robotDirection),
                robotPosition.y() + velocity * Math.sin(robotDirection)
        );
    }

    public void makeMove() {
        double distance = distance(targetPosition, robotPosition);
        if (distance < 0.5) {
            return;
        }
        double angleToTarget = angleTo(targetPosition, robotPosition);
        double angle = asNormalizedRadians(angleToTarget - robotDirection);
        double angularVelocity = calculateAngularVelocity(angleToTarget, robotDirection, maxAngularVelocity);

        double angleSlowingFactor = 1 - Math.abs(angle) / Math.PI;
        double distanceSlowingFactor = Math.max(0.1, distance / 100);
        distanceSlowingFactor = angleSlowingFactor > 0.95 ? 1 : distanceSlowingFactor;
        double slowingFactor = angleSlowingFactor * distanceSlowingFactor;

        moveRobot(maxVelocity * slowingFactor, angularVelocity);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastPositionUpdate >= 1000) {
            lastPositionUpdate = currentTime;
            setChanged();
            notifyObservers();
            clearChanged();
        }
    }

    public void draw(Graphics2D g) {
        Point robotIntCenter = robotPosition.toPoint();
        AffineTransform t = AffineTransform.getRotateInstance(
                this.getRobotDirection(),
                robotIntCenter.x(),
                robotIntCenter.y()
        );
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        Visualization.fillOval(g, robotIntCenter.x(), robotIntCenter.y(), 30, 10);
        g.setColor(Color.BLACK);
        Visualization.drawOval(g, robotIntCenter.x(), robotIntCenter.y(), 30, 10);
        g.setColor(Color.WHITE);
        Visualization.fillOval(g, robotIntCenter.x() + 10, robotIntCenter.y(), 5, 5);
        g.setColor(Color.BLACK);
        Visualization.drawOval(g, robotIntCenter.x() + 10, robotIntCenter.y(), 5, 5);
    }

    public DoublePoint getPosition() {
        return robotPosition;
    }

    @Override
    public void handleEvent(Event event) {}

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Target) {
            setTargetPosition(((Target) o).getPosition());
        }
    }
}