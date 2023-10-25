package main.java.view;

import model.Entity;
import model.Robot;
import model.Target;

import java.awt.*;

public class TargetDrawer extends GameDrawer {
    @Override
    public void draw(Graphics2D g, Entity entity) {
        Target target = ((Robot) entity).getTarget();
        g.setColor(Color.GREEN);
        fillOval(g, target.getX(), target.getY(), 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, target.getX(), target.getY(), 5, 5);
    }
    @Override
    public Class<?> getDrawingType() {
        return Target.class;
    }
}
