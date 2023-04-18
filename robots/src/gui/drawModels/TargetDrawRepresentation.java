package gui.drawModels;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TargetDrawRepresentation implements Drawable{

    private int m_x;
    private int m_y;

    public TargetDrawRepresentation(int m_x, int m_y) {
        update(m_x,m_y);
    }

    public void update(int m_x, int m_y) {
        this.m_x = m_x;
        this.m_y = m_y;
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        Drawable.fillOval(g, m_x, m_y, 5, 5);
        g.setColor(Color.BLACK);
        Drawable.drawOval(g, m_x, m_y, 5, 5);
    }
}
