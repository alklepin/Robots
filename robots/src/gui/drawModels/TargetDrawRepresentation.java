package gui.drawModels;

import models.Vector;
import models.TargetModel;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static utils.DrawUtils.*;
import static utils.MathUtils.*;

public class TargetDrawRepresentation implements Drawable {

    private TargetModel m_model;

    public TargetDrawRepresentation(TargetModel model) {
        m_model=model;
    }


    @Override
    public void draw(Graphics2D g) {
        Vector targetCoord=m_model.getPos();
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, round(targetCoord.x) ,round(targetCoord.y) , 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, round(targetCoord.x) ,round(targetCoord.y),  5, 5);
    }


}
