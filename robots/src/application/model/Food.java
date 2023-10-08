package application.model;

import java.awt.*;

public class Food {
    private volatile int x;
    private volatile int y;

    public Food(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Food()
    {
        this.x = 100;
        this.y = 100;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setFoodPosition(Point p)
    {
        setX(p.x);
        setY(p.y);
    }

    protected Point getFoodPosition()
    {
        return new Point(getX(), getY());
    }

    public boolean isPositionCorrect(Dimension dimension)
    {
        return this.x <= dimension.width && this.y <= dimension.height;
    }
}
