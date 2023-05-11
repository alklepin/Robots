package gui.drawModels;
import models.ModelCoordinate;
import models.RobotModel;

import static utils.DrawUtils.*;
import static utils.MathUtils.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class RobotRepresentation implements Drawable{
    private RobotModel m_model;

    public RobotRepresentation(RobotModel model) {
        m_model=model;
    }


    @Override
    public void draw(Graphics2D g) {
        ModelCoordinate coord=m_model.getPos();
        int robotCenterX = round(coord.x);
        int robotCenterY = round(coord.y);
        AffineTransform t = AffineTransform.getRotateInstance(coord.z, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }



}
