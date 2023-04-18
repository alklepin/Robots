package gui.drawModels;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class DefaultRobot implements Drawable{
    private double m_posX;
    private double m_posY;
    private double m_dir;

    public DefaultRobot(double m_posX, double m_posY, double m_dir) {
        update(m_posX,m_posY,m_dir);
    }
    public void update(double m_posX, double m_posY, double m_dir) {
        this.m_posX = m_posX;
        this.m_posY = m_posY;
        this.m_dir = m_dir;
    }

    @Override
    public void draw(Graphics2D g) {
        int robotCenterX = Drawable.round(m_posX);
        int robotCenterY = Drawable.round(m_posY);
        AffineTransform t = AffineTransform.getRotateInstance(m_dir, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        Drawable.fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        Drawable.drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        Drawable.fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        Drawable.drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }



}
