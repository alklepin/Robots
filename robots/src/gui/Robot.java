package gui;

public class Robot {

    private volatile double XCoordinate;
    private volatile double YCoordinate;
    private volatile double Direction;

    public void setXCoordinate(double XCoordinate) {
        this.XCoordinate = XCoordinate;
    }

    public void setYCoordinate(double YCoordinate) {
        this.YCoordinate = YCoordinate;
    }

    public void setDirection(double direction) {
        Direction = direction;
    }

    public double getXCoordinate() {
        return XCoordinate;
    }

    public double getYCoordinate() {
        return YCoordinate;
    }

    public double getDirection() {
        return Direction;
    }

    public Robot(double XCoordinate, double YCoordinate, double direction) {
        this.XCoordinate = XCoordinate;
        this.YCoordinate = YCoordinate;
        Direction = direction;
    }
}
