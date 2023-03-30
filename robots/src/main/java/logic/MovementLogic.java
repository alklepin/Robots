package logic;


import models.Robot;
import models.Target;

public class MovementLogic {

    private static final int duration = 10;
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public boolean isNeedToMoveRobot(Robot robot, Target target) {
        double distance = MathLogic.distance(
                target.positionX, target.positionY, robot.positionX, robot.positionY
        );
        return (distance >= 0.5);
    }

    private double getAngularVelocity(Robot robot, Target target) {
        double angleToTarget = MathLogic.angleTo(robot.positionX, robot.positionY, target.positionX, target.positionY);
        double angularVelocity = 0;
        if (angleToTarget > robot.direction) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < robot.direction) {
            angularVelocity = -maxAngularVelocity;
        }
        return angularVelocity;
    }

    public void moveRobot(Robot robot, Target target) {
        double angularVelocity = MathLogic.applyLimits(
                getAngularVelocity(robot, target), -maxAngularVelocity, maxAngularVelocity
        );
        double newX = robot.positionX + maxVelocity / angularVelocity *
                (Math.sin(robot.direction + angularVelocity * duration) -
                        Math.sin(robot.direction));
        if (!Double.isFinite(newX)) {
            newX = robot.positionX + maxVelocity * duration * Math.cos(robot.direction);
        }
        double newY = robot.positionY - maxVelocity / angularVelocity *
                (Math.cos(robot.direction + angularVelocity * duration) -
                        Math.cos(robot.direction));
        if (!Double.isFinite(newY)) {
            newY = robot.positionY + maxVelocity * duration * Math.sin(robot.direction);
        }
        double newDirection = MathLogic.asNormalizedRadians(robot.direction + angularVelocity * duration);

        robot.positionX = newX;
        robot.positionY = newY;
        robot.direction = newDirection;
    }
}
