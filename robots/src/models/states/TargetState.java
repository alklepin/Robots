package models.states;

import java.io.Serializable;

public class TargetState implements TargetStateReader, Serializable {
    private double x;
    private double y;

    public TargetState(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
