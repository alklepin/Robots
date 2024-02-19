package main.java.view;

import main.java.model.Entity;

import java.awt.*;

public abstract class GameDrawer {
    protected static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    protected static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    public abstract void draw(Graphics2D g, Entity entity);


    public abstract Class<?> getDrawingType();
}
