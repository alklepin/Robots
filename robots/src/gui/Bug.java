package gui;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bug extends GameObject {

    public volatile double Direction = 0;
    public final double BugSize = 60;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public Bug(double x, double y, ImageView pict)
    {
        super(x, y, pict);
    }

}
