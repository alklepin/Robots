package gui.drawModels;


import models.TargetModel;
import models.states.TargetStateReader;

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
        TargetStateReader targetCoord=m_model.getState();
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, round(targetCoord.getX()) ,round(targetCoord.getY()) , 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, round(targetCoord.getX()) ,round(targetCoord.getY()),  5, 5);
    }


}
