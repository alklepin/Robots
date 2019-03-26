package gui;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;


public class Target extends GameObject {

    public final double TargetSize = 30;

    private void setTargetPosition(double x, double y) {
        X_Position = x;
        Y_Position = y;
    }

    public Target(double x, double y, ImageView pict)
    {
        super(x, y, pict);
    }
}
