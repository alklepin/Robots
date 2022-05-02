package gui;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class RoboPainter {
    private static Graphics2D g2d;

    public RoboPainter(Graphics2D _g2d) {
        g2d = _g2d;
    }

    private static void fillOval(int centerX, int centerY, int diam1, int diam2)
    {
        g2d.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(int centerX, int centerY, int diam1, int diam2)
    {
        g2d.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    public void drawRobot(int x, int y, double direction)
    {
        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g2d.setTransform(t);
        g2d.setColor(Color.MAGENTA);
        fillOval(x, y, 30, 10);
        g2d.setColor(Color.BLACK);
        drawOval(x, y, 30, 10);
        g2d.setColor(Color.WHITE);
        fillOval(x + 10, y, 5, 5);
        g2d.setColor(Color.BLACK);
        drawOval(x + 10, y, 5, 5);
    }

    public void drawTarget(int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g2d.setTransform(t);
        g2d.setColor(Color.GREEN);
        fillOval(x, y, 5, 5);
        g2d.setColor(Color.BLACK);
        drawOval(x, y, 5, 5);
    }
}
