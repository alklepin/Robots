package main.java.model;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;

public class Target implements Entity {
    private volatile int x;
    private volatile int y;
    private Target target;

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
        return x;
    }

    public int getY() {
        return y;
    }

    public void setTargetPosition(Point p) {
        setX(p.x);
        setY(p.y);
        this.target = new Target(p.x, p.y);
    }



    public boolean isPositionCorrect(Dimension dimension) {
        return this.x <= dimension.width && this.y <= dimension.height;
    }

    @Override
    public void update() {

    }

    @Override
    public void onStart(PropertyChangeSupport publisher) {

    }

    @Override
    public void onFinish(PropertyChangeSupport publisher) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }


    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
