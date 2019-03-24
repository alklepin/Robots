package gui;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Wall extends GameObject {

    public Point Position;
    public Point SecondPosition;
    private boolean Alive;
    private Image Picture;

    public Wall(int x, int y, int width, int height){
        Position = new Point(x, y);
        SecondPosition = new Point(width, height);
    }

    private void fillRect(Graphics g, int x, int y, int width, int height)
    {
        g.fillRect(x, y, width, height);
    }

    private void drawRect(Graphics g, int x, int y, int width, int height)
    {
        g.drawRect(x, y, width, height);
    }

    public void draw(Graphics2D g)
    {
        int width = SecondPosition.x - Position.x;
        int height = SecondPosition.y - Position.y;
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.BLACK);
        fillRect(g, Position.x, Position.y, width, height);
        g.setColor(Color.BLACK);
        drawRect(g, Position.x, Position.y, width, height);
    }
    public void move()
    {

    }
}
