package application.view.draw;

import application.model.Food;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class FoodDrawer extends Drawer
{
    public void draw(Graphics2D g, Food food)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, food.getX(), food.getY(), 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, food.getX(), food.getY(), 5, 5);
    }
}
