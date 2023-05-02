package org.iffomko.gui.models;

import java.util.Observable;
import java.util.Observer;

/**
 * <p>Модель робота, которая умеет передвигать робота и возвращать его актуальные коордианты</p>
 */
public class Robot extends Observable {
    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    private volatile double x = 100;
    private volatile double y = 100;
    private volatile double direction = 0;

    public static final String KEY_ROBOT_POSITION_CHANGED = "position is changed";

    /**
     * Высчитывает расстояние между двумя точками (x1, y1) и (x2, y2)
     * @param x1 - координата x первой точки
     * @param y1 - координата y первой точки
     * @param x2 - координата x второй точки
     * @param y2 - координата y второй точки
     * @return - расстояние
     */
    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    /**
     * Вычисляет тета-угол между двумя точками
     * @param fromX - координата по оси OX первой точки
     * @param fromY - координата по оси OY первой точки
     * @param toX - координата по оси OX второй точки
     * @param toY - координата по оси OY второй точки
     * @return - тета-угол
     */
    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        if (diffY == 0) {
            return 0;
        }

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    /**
     * Приводит входное значение <code>value</code> к нормализованным значениям, т. е. чтобы не вышло за границы <code>[min, max]</code>
     * @param value - значение, которое нужно привести
     * @param min - минимальное значение, которое может принимать <code>value</code>
     * @param max - максимальное значение, которое может принимать <code>value</code>
     * @return - приведенное значение
     */
    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    /**
     * Делает угол положительным (0 <= angle < 2*Math.PI)
     * @param angle - входящий угол в радианах
     * @return - нормализованный угол
     */
    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI)
        {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    /**
     * Передвигает робота
     * @param targetPositionX - координата к конечной точки по оси OX
     * @param targetPositionY - координата к конечной точки по оси OY
     * @param duration - промежуток, в течении которого он двигается
     */
    public void move(int targetPositionX, int targetPositionY, int duration)
    {
        double distance = distance(targetPositionX, targetPositionY, x, y);

        if (distance < 0.5) {
            return;
        }

        double velocity = maxVelocity;
        double angleToTarget = angleTo(x, y, targetPositionX, targetPositionY);
        double angularVelocity = 0;

        if (angleToTarget > direction) {
            angularVelocity = maxAngularVelocity;
        }

        if (angleToTarget < direction) {
            angularVelocity = -maxAngularVelocity;
        }

        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

        double newX = x + velocity / angularVelocity * (Math.sin(direction  + angularVelocity * duration) - Math.sin(direction));

        if (!Double.isFinite(newX)) {
            newX = x + velocity * duration * Math.cos(direction);
        }

        double newY = y - velocity / angularVelocity * (Math.cos(direction  + angularVelocity * duration) - Math.cos(direction));

        if (!Double.isFinite(newY)) {
            newY = y + velocity * duration * Math.sin(direction);
        }

        x = newX;
        y = newY;
        direction = asNormalizedRadians(direction + angularVelocity * duration);;

        setChanged();
        notifyObservers(KEY_ROBOT_POSITION_CHANGED);
        clearChanged();
    }

    /**
     * @return - возвращает координату робота по оси OX
     */
    public double getX() {
        return x;
    }

    /**
     * @return - возвращает координату робота по оси OY
     */
    public double getY() {
        return y;
    }

    /**
     * @return - возвращает угол направления робота в радианах
     */
    public double getDirection() {
        return direction;
    }
}
