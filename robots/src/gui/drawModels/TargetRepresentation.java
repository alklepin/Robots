package gui.drawModels;

import models.ModelCoordinate;
import models.TargetModel;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static utils.DrawUtils.*;
import static utils.MathUtils.*;

public class TargetRepresentation implements Drawable {

    private TargetModel m_model;

    public TargetRepresentation(TargetModel model) {
        m_model=model;
    }


    @Override
    public void draw(Graphics2D g) {
        ModelCoordinate targetCoord=m_model.getPos();
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, round(targetCoord.x) ,round(targetCoord.y) , 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, round(targetCoord.x) ,round(targetCoord.y),  5, 5);
    }


}
