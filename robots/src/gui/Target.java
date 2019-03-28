package gui;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;


public class Target extends GameObject {

    public void setTargetPosition(double x, double y) {
        X_Position = x;
        Y_Position = y;
    }

    Target(double x, double y, String path)
    {
        super(x, y, path, 30);
        Direction = - Math.PI / 2;
    }
}
