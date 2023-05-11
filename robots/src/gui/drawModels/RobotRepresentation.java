package gui.drawModels;
import models.RobotModel;
import models.states.RobotStateReader;

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
        RobotStateReader state=m_model.getState();
        int robotCenterX = round(state.getX());
        int robotCenterY = round(state.getY());
        AffineTransform t = AffineTransform.getRotateInstance(state.getDir(), robotCenterX, robotCenterY);
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
