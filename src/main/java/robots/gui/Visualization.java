package robots.gui;

import robots.domain.DoublePoint;
import robots.domain.Robot;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;

import java.awt.geom.AffineTransform;

public class Visualization {
    public void drawRobot(Graphics2D g, Robot robot) {
        int robotCenterX = robot.getRobotPosition().getIntX();
        int robotCenterY = robot.getRobotPosition().getIntY();
        double direction = robot.getRobotDirection();


        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    }

    public void drawTarget(Graphics2D g, DoublePoint point) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, point.getIntX(), point.getIntY(), 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, point.getIntX(), point.getIntY(), 5, 5);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
}
