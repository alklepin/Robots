package main.java.model;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class Target implements Entity {
    private volatile int x;
    private volatile int y;

    public Target() {
        this.x = 100;
        this.y = 100;
    }

    public Target(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setTargetPosition(Point p) {
        this.setX(p.x);
        this.setY(p.y);
    }

    public boolean isPositionCorrect(Dimension dimension) {
        return this.x <= dimension.width && this.y <= dimension.height;
    }

    public void update() {
    }

    public void onStart(PropertyChangeSupport publisher) {
        publisher.addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
    }
}
