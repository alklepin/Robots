package main.java.model;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameModel implements Entity {
    public static final String CHANGE_SATIETY_EVENT = "change satiety";
    public static final String SET_DIMENSION_EVENT = "set dimension";
    public static final String NEW_POINT_EVENT = "new point";
    private final List<Entity> entities = this.initStateOfBacterias(2);
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public GameModel() {
        Timer timer = initTimer();
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("timer");
                GameModel.this.support.firePropertyChange("change satiety", (Object)null, -10);
            }
        }, 0L, 1500L);
    }

    private static Timer initTimer() {
        return new Timer("satiety generator", true);
    }

    public void setDimension(Dimension dimension) {
        this.support.firePropertyChange("set dimension", (Object)null, dimension);
    }

    public Dimension getDimension() {
        return ((Robot)this.entities.get(0)).getDimension();
    }

    public void updateModel() {
        Iterator var1 = this.entities.iterator();

        while(var1.hasNext()) {
            Entity entity = (Entity)var1.next();
            entity.update();
        }

    }

    public List<Entity> getEntities() {
        return this.entities;
    }

    public void setTarget(Point point) {
        this.support.firePropertyChange("new point", (Object)null, point);
    }

    public List<Entity> initStateOfBacterias(int amount) {
        List<Entity> entityList = new ArrayList();

        for(int i = 0; i < amount; ++i) {
            Robot robot = new Robot(Math.random() * 400.0, Math.random() * 400.0);
            robot.setTarget(new Point((int)(Math.random() * 400.0), (int)(Math.random() * 400.0)));
            robot.onStart(this.support);
            entityList.add(robot);
        }

        return entityList;
    }

    public void update() {
    }

    public void onStart(PropertyChangeSupport publisher) {
    }

    public void propertyChange(PropertyChangeEvent evt) {
    }
}
