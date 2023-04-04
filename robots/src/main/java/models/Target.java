package models;

import java.awt.*;

public class Target {
    public volatile int positionX = 150;
    public volatile int positionY = 100;

    public void setTargetPosition(Point p)
    {
        positionX = p.x;
        positionY = p.y;
    }
}