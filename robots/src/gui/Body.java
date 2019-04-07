package gui;

public class Body extends GameObject {
    public double current_X;
    public double current_Y;

    Body(double current_X, double current_Y)
    {
        super(current_X, current_Y, "11.png", FieldCell.translateFactor);
        this.current_X = current_X;
        this.current_Y = current_Y;

    }
}
