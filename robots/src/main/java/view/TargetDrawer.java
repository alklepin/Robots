package main.java.view;

import java.awt.Color;
import java.awt.Graphics2D;
import main.java.model.Entity;
import main.java.model.Target;

public class TargetDrawer extends GameDrawer {
    public TargetDrawer() {
    }

    public void draw(Graphics2D g, Entity entity) {
        Target target = (Target)entity;
        if (target != null) {
            g.setColor(Color.GREEN);
            fillOval(g, target.getX(), target.getY(), 5, 5);
            g.setColor(Color.BLACK);
            drawOval(g, target.getX(), target.getY(), 5, 5);
        }

    }

    public Class<?> getDrawingType() {
        return Target.class;
    }
}
