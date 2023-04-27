package gui;

public class Robot {

    private volatile double xCoordinate;
    private volatile double yCoordinate;
    private volatile double direction;

    public void setXCoordinate(double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public void setYCoordinate(double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getXCoordinate() {
        return xCoordinate;
    }

    public double getYCoordinate() {
        return yCoordinate;
    }

    public double getDirection() {
        return direction;
    }

    public Robot(double xCoordinate, double yCoordinate, double direction) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.direction = direction;
    }
}
