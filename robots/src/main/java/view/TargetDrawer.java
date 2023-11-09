package main.java.view;

import main.java.model.Entity;
import main.java.model.Target;

import java.awt.*;

public class TargetDrawer extends GameDrawer {
    public void draw(Graphics2D g, Entity entity) {
        Target target = ((Target) entity).getTarget();
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
