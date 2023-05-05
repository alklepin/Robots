package models;

public class Vector {
    public double x;
    public double y;
    public double z=0;
    public Vector(double _x, double _y){
        x=_x;
        y=_y;
    }
    public Vector(double _x, double _y, double _z){
        x=_x;
        y=_y;
        z=_z;
    }
}
