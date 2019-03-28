package gui;

import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bug extends GameObject {

    public final double maxVelocity = 0.1;
    public final double maxAngularVelocity = 0.001;

    public Bug(double x, double y, String path)
    {
        super(x, y, path, 60);
    }

}
