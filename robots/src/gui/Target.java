package gui;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static gui.Constants.GameVisualizerConstants.*;
import static gui.Constants.GameVisualizerConstants.TARGET_DIAMETER;

public class Target extends GameModel implements Moveable {

    private volatile double horizontalVelocity;
    private volatile double verticalVelocity;

    public double getHorizontalVelocity() {
        return horizontalVelocity;
    }

    public void setHorizontalVelocity(double horizontalVelocity) {
        this.horizontalVelocity = horizontalVelocity;
    }

    public double getVerticalVelocity() {
        return verticalVelocity;
    }

    public void setVerticalVelocity(double verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }

    public Target(double xCoordinate, double yCoordinate) {
        super(xCoordinate, yCoordinate);
        this.horizontalVelocity = 0;
        this.verticalVelocity = 0;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform t = AffineTransform.getRotateInstance(TARGET_THETA, TARGET_ANCHORX, TARGET_ANCHORY);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        int targetCenterX = MathModule.round(getxCoordinate());
        int targetCenterY = MathModule.round(getyCoordinate());
        GameVisualizer.fillOval(g, targetCenterX, targetCenterY, TARGET_DIAMETER, TARGET_DIAMETER);
        g.setColor(Color.BLACK);
        GameVisualizer.drawOval(g, targetCenterX, targetCenterY, TARGET_DIAMETER, TARGET_DIAMETER);
    }

    @Override
    public void move() {
        double newTargetXCoordinate = getxCoordinate() + getHorizontalVelocity() * TIMER_UPDATE_PERIOD;
        double newTargetYCoordinate = getyCoordinate() + getVerticalVelocity() * TIMER_UPDATE_PERIOD;
        setxCoordinate(newTargetXCoordinate);
        setyCoordinate(newTargetYCoordinate);
    }
}
