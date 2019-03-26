package gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Bug extends GameObject {

    private volatile double direction = 0;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public Bug(Point position, Image picture)
    {
        Picture = picture;
        Position = position;
    }

    public Bug(Point position,String path)
    {
        try {
            Picture = ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        Position = position;
    }
}
