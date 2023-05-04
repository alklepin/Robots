package models;

public class CoordPair {
    public double x;
    public double y;
    public double z=0;
    public CoordPair(double _x,double _y){
        x=_x;
        y=_y;
    }
    public CoordPair(double _x,double _y,double _z){
        x=_x;
        y=_y;
        z=_z;
    }
}
