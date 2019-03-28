package gui;

public class Mine extends GameObject{
    public Mine(double x, double y){
        super(x, y, "mine.jpg", FieldCell.translateFactor);
        FieldCell cell = FieldCell.getCell(X_Position, Y_Position);
        X_Position = cell.X * FieldCell.translateFactor;
        Y_Position = cell.Y * FieldCell.translateFactor;
        Direction = - Math.PI / 2;
        Picture.setX(X_Position);
        Picture.setY(Y_Position);
        Picture.setRotate(Direction);
    }
}
