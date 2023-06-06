package robots.domain.events;

import robots.domain.Point;

public class MouseEvent extends Event {
    public Point point;

    public MouseEvent(int x, int y) {
        super();
        this.point = new Point(x, y);
    }
}
