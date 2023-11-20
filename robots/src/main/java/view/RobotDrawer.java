package main.java.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import main.java.model.Entity;
import main.java.model.Robot;
import main.java.model.TypeRobot;

public class RobotDrawer extends GameDrawer {
    public RobotDrawer() {
    }

    public void draw(Graphics2D g, Entity entity) {
        Robot robot = (Robot)entity;
        AffineTransform oldTransform = g.getTransform();
        int robotCenterX = (int)Math.round(robot.getPositionX());
        int robotCenterY = (int)Math.round(robot.getPositionY());
        AffineTransform l = new AffineTransform(oldTransform);
        AffineTransform t = AffineTransform.getRotateInstance(robot.getRobotDirection(), (double)robotCenterX, (double)robotCenterY);
        l.concatenate(t);
        g.setTransform(l);
        g.setColor(robot.getType().getColor());
        if (robot.getType().equals(TypeRobot.HUNGRY)) {
            fillOval(g, robotCenterX, robotCenterY, 20, 10);
            g.setColor(Color.BLACK);
            drawOval(g, robotCenterX, robotCenterY, 20, 10);
            g.setColor(Color.YELLOW);
        }

        if (robot.getType().equals(TypeRobot.CALM)) {
            fillOval(g, robotCenterX, robotCenterY, 25, 15);
            g.setColor(Color.BLACK);
            drawOval(g, robotCenterX, robotCenterY, 25, 15);
            g.setColor(Color.WHITE);
        }

        if (robot.getType().equals(TypeRobot.DEAD)) {
            fillOval(g, robotCenterX, robotCenterY, 20, 10);
            g.setColor(Color.WHITE);
            drawOval(g, robotCenterX, robotCenterY, 20, 10);
            g.setColor(Color.BLACK);
        }

        fillOval(g, robotCenterX + 7, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 7, robotCenterY, 5, 5);
        g.setTransform(oldTransform);
    }

    public Class<?> getDrawingType() {
        return Robot.class;
    }
}
