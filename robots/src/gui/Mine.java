package gui;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Mine {
    public Point Position;
    private boolean Alive;
    private Image Picture;

    public Mine(int x, int y){
        Position = new Point(x, y);
    }

    private static void fillOval(Graphics g, int centerX, int centerY)
    {
        g.fillOval(centerX - 10 / 2, centerY - 10 / 2, 10, 10);
    }

    private static void drawOval(Graphics g, int centerX, int centerY)
    {
        g.drawOval(centerX - 9 / 2, centerY - 9 / 2, 9, 9);
    }

    public void draw(Graphics2D g)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.RED);
        fillOval(g, Position.x, Position.y);
        g.setColor(Color.BLACK);
        drawOval(g, Position.x, Position.y);
    }
    public void move()
    {

    }
}
