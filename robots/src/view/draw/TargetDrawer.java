package application.view.draw;

import application.model.Bacteria;
import application.model.Entity;
import application.model.Target;

import java.awt.*;

public class TargetDrawer extends Drawer
{
    @Override
    public void draw(Graphics2D g, Entity entity)
    {
        Target target = ((Bacteria) entity).getTarget();
        g.setColor(Color.GREEN);
        fillOval(g, target.getX(), target.getY(), 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, target.getX(), target.getY(), 5, 5);
    }

    @Override
    public Class<?> getDrawingType()
    {
        return Target.class;
    }
}
