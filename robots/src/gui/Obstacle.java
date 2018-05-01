package gui;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

public class Obstacle {

    private Point leftUp;
    private Point rightUp;
    private Point leftDown;
    private Point rightDown;

    public Obstacle(Point p) {
        leftUp = new Point(p.x-25,p.y-25);
        leftDown = new Point(p.x+25, p.y-25);
        rightUp = new Point(p.x-25, p.y+25);
        rightDown = new Point(p.x+25,p.y+25);
    }

    private Color randomColor(){
        Color[] colors = new Color[]{Color.BLACK,Color.GREEN,Color.MAGENTA,Color.BLUE,Color.CYAN,Color.LIGHT_GRAY,Color.ORANGE,Color.RED,Color.YELLOW};
        Random randomColour = new Random();
        int color = randomColour.nextInt(colors.length-1 +1);
        return colors[color];
    }

    void paint(Graphics g){
        g.setColor(randomColor());
        g.fillRect(this.leftUp.x,this.leftUp.y,50,50);
        //g.drawRect(this.leftUp.x,this.leftUp.y,50,50);
    }

    boolean hasInBorder(Point p){
        return  (p.x>this.leftUp.x&&p.y>this.leftUp.y&&
                p.x<this.rightDown.x&&p.y<this.rightDown.y);
    }

    public Point getLeftUp() {
        return leftUp;
    }

    public Point getRightUp() {
        return rightUp;
    }

    public Point getLeftDown() {
        return leftDown;
    }

    public Point getRightDown() {
        return rightDown;
    }
}
