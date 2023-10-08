package application.view.draw;

import application.model.Bot;
import application.model.additionals.Condition;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class BotDrawer extends Drawer {
    public void draw(Graphics2D g, Bot bot)
    {
        AffineTransform oldTransform = g.getTransform();
        int robotCenterX = (int) Math.round(bot.getPositionX());
        int robotCenterY = (int) Math.round(bot.getPositionY());
        AffineTransform l = new AffineTransform(oldTransform);
        AffineTransform t = AffineTransform.getRotateInstance(bot.getBotDirection(), robotCenterX, robotCenterY);
        l.concatenate(t);
        g.setTransform(l);
        g.setColor(bot.getCondition().getColor());

        if (bot.getCondition().equals(Condition.HUNGRY))
        {
            fillOval(g, robotCenterX, robotCenterY, 20, 10);
            g.setColor(Color.BLACK);
            drawOval(g, robotCenterX, robotCenterY, 20, 10);
            g.setColor(Color.YELLOW);
        }

        if (bot.getCondition().equals(Condition.CALM))
        {
            fillOval(g, robotCenterX, robotCenterY, 20, 10);
            g.setColor(Color.BLACK);
            drawOval(g, robotCenterX, robotCenterY, 20, 10);
            g.setColor(Color.WHITE);
        }

        if (bot.getCondition().equals(Condition.DEAD))
        {
            fillOval(g, robotCenterX, robotCenterY, 20, 10);
            g.setColor(Color.BLACK);
            drawOval(g, robotCenterX, robotCenterY, 20, 10);
            g.setColor(Color.MAGENTA);
        }

        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setTransform(oldTransform);
    }
}
