package gui;

public class Bug extends GameObject {

    private final double maxVelocity = 0.2;

    private double duration = 10;

    Bug(double x, double y)
    {
        super(x, y, "bug_1.png", FieldCell.translateFactor);
    }


    public void onModelUpdateEvent(Double direction) {
        Direction = direction;
        double newXPos = X_Position + maxVelocity * Math.cos(direction);
        double newYPos = Y_Position + maxVelocity * Math.sin(direction);
        if (newXPos >= 0 & newXPos <= (Field.Width - Size))
            X_Position = newXPos;
        if (newYPos >= 0 & newYPos <= Field.Height - Size)
            Y_Position = newYPos;
    }
}
