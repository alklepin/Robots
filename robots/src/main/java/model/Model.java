package model;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Model {
    private final List<Entity> entities;
    private final PropertyChangeSupport support;

    public Model() {
        this.support = new PropertyChangeSupport(this);
        this.entities = initStateOfBacterias(2);

        Timer timer = initTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("timer");
                support.firePropertyChange("change satiety", null, -10);
            }
        }, 0, 1500);
    }

    private static java.util.Timer initTimer() {
        return new Timer("satiety generator", true);
    }

    public void setDimension(Dimension dimension) {
        support.firePropertyChange("set dimension", null, dimension);
    }

    public Dimension getDimension() {
        return ((Robot) entities.get(0)).getDimension();
    }

    public void updateModel() {
        for (Entity entity : entities) {
            entity.update();
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setTarget(Point point) {
        support.firePropertyChange("new point", null, point);
    }

    public List<Entity> initStateOfBacterias(int amount) {
        List<Entity> entityList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Robot robot = new Robot(Math.random() * 400, Math.random() * 400);
            robot.setTarget(new Point((int) (Math.random() * 400), (int) (Math.random() * 400)));
            robot.onStart(support);
            entityList.add(robot);
        }
        return entityList;
    }
}


