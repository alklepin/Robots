package org.iffomko.gui.models;

import java.awt.*;
import java.util.Observable;


/**
 * <p>Модель цели, к которой стремится робот</p>
 */
public class Target extends Observable {
    private volatile int x = 150;
    private volatile int y = 100;

    public static final String KEY_TARGET_POSITION_CHANGED = "target position changed";

    /**
     * Устанавливает позицию для элемента, которому стремится робот
     * @param p - точка, к которой робот стремится
     */
    public void setTargetPosition(Point p) {
        x = p.x;
        y = p.y;

        setChanged();
        notifyObservers();
        clearChanged();
    }

    /**
     * @return - возвращает координату точки назначения по оси OX
     */
    public int getX() {
        return x;
    }

    /**
     * @return - возвращает координату точки назначения по оси OY
     */
    public int getY() {
        return y;
    }
}
