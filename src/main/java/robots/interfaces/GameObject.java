package robots.interfaces;

import robots.domain.DoublePoint;
import robots.domain.events.Event;

import java.awt.Graphics2D;
import java.awt.Point;

public interface GameObject {
    void draw(Graphics2D g);
    DoublePoint getPosition();
    void handleEvent(Event event);

    void makeMove();
}
