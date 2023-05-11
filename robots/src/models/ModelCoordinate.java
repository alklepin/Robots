package models;

public class ModelCoordinate {
    public double x;
    public double y;
    public double z=0;
    public ModelCoordinate(double _x, double _y){
        x=_x;
        y=_y;
    }
    public ModelCoordinate(double _x, double _y, double _z){
        x=_x;
        y=_y;
        z=_z;
    }
}
