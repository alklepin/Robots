package robots.domain;

import java.util.Observable;


public class Robot extends Observable {
    private final DoublePoint robotPosition = new DoublePoint(100, 100);
    private volatile double robotDirection = 0;

    private volatile DoublePoint targetPosition = new DoublePoint(150, 100);

    private static final double maxVelocity = 1;
    private static final double maxAngularVelocity = 0.01;
    private long lastPositionUpdate;

    public DoublePoint getRobotPosition() {
        return robotPosition;
    }

    public double getRobotDirection() {
        return robotDirection;
    }

    public DoublePoint getTargetPosition() {
        return targetPosition;
    }

    private static double distance(DoublePoint target, DoublePoint robot) {
        double diffX = target.getX() - robot.getX();
        double diffY = target.getY() - robot.getY();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(DoublePoint target, DoublePoint robot) {
        double diffX = target.getX() - robot.getX();
        double diffY = target.getY() - robot.getY();

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public void setTargetPosition(DoublePoint p) {
        targetPosition = p;
        setChanged();
        notifyObservers();
    }


    private static double calculateAngularVelocity(double angle, double robotDirection, double maxAngularVelocity) {
        double diff = angle - robotDirection;
        diff = asNormalizedRadians(diff);

        return Math.signum(diff) * Math.min(maxAngularVelocity, Math.abs(diff));
    }

public void onModelUpdateEvent() {
    double distance = distance(targetPosition, robotPosition);
    if (distance < 0.5) {
        setChanged();
        notifyObservers();
        clearChanged();
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

        robotPosition.setX(robotPosition.getX() + velocity * Math.cos(robotDirection));
        robotPosition.setY(robotPosition.getY() + velocity * Math.sin(robotDirection));
    }
}