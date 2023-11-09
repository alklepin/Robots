package main.java.model;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Model {
    private final List<Entity> entities;
    private final PropertyChangeSupport support;

    public static final String NEW_POINT_EVENT = "new point";
    public static final String CHANGE_SATIETY = "change satiety";
    public static final String SATIETY_GENERATOR = "satiety generator";
    public static final String SET_DIMENSION = "set dimension";


    public Model() {
        this.support = new PropertyChangeSupport(this);
        this.entities = initStateOfBacterias(2);

        Timer timer = initTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("timer");
                support.firePropertyChange(CHANGE_SATIETY, null, -10);
            }
        }, 0, 1500);
    }

    private static Timer initTimer() {
        return new Timer(SATIETY_GENERATOR, true);
    }

    public void setDimension(Dimension dimension) {
        support.firePropertyChange(SET_DIMENSION, null, dimension);
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
        support.firePropertyChange(NEW_POINT_EVENT, null, point);
    }

    public List<Entity> initStateOfBacterias(int amount) {
        List<Entity> entityList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Robot robot = new Robot(Math.random() * 400, Math.random() * 400);
            robot.setTargetPosition(new Point((int) (Math.random() * 400), (int) (Math.random() * 400)));
            robot.onStart(support);
            entityList.add((Entity) robot);
        }
        return entityList;
    }
}


