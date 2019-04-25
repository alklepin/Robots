package gui;

import java.util.ArrayList;

public class Zombie extends GameObject {
    private double lastDirection = 0;
    private int counter = 0;
    private final int maxCounter = 500;


    public Zombie(double x, double y) {
        super(x, y, "zombie.png", FieldCell.translateFactor);
        Direction = Directions.UP;
    }

    private void moving(double nextDirection)
    {
        X_Position = X_Position + Config.ZOMBIE_SPEED * Math.cos(nextDirection);
        Y_Position = Y_Position + Config.ZOMBIE_SPEED * Math.sin(nextDirection);
    }
    public void move(ArrayList<Double> possibleDirections)
    {
        counter++;
        if (!possibleDirections.contains(lastDirection) || counter >= maxCounter)
        {
            int count = possibleDirections.size() - 1;
            int res = (int) Math.round(count * Math.random());
            lastDirection = possibleDirections.get(res);
            counter = 0;
        }
        moving(lastDirection);
    }
}
