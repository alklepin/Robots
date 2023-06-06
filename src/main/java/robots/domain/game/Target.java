package robots.domain.game;

import robots.domain.DoublePoint;
import robots.domain.events.Event;
import robots.domain.events.MouseEvent;
import robots.interfaces.GameObject;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.util.Observable;

public class Target extends Observable implements GameObject {
    private DoublePoint position;

    public Target(double x, double y) {
        System.out.println("New target created" + x + " " + y);
        position = new DoublePoint(x, y);
    }

    private void setPosition(DoublePoint p) {
        position = p;
        setChanged();
        notifyObservers();
    }

    @Override
    public void draw(Graphics2D g) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        g.fillOval((int) position.x() - 5, (int) position.y() - 5, 10, 10);
        g.setColor(Color.BLACK);
    }

    @Override
    public DoublePoint getPosition() {
        return position;
    }


    @Override
    public void handleEvent(Event event) {
        if (event instanceof MouseEvent mouseEvent) {
            System.out.println("Target (mouse): " + mouseEvent.point);
            setPosition(mouseEvent.point.toDoublePoint());
        }
    }

    @Override
    public void makeMove() {}
}
