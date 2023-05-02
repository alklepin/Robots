package org.iffomko.gui.views;


import org.iffomko.gui.models.Robot;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * <p>Класс, который рисует робота</p>
 */
public class RobotVisualizer extends Visualizer {
    private final Robot robot;

    /**
     * <p>Получает модель робота для отслеживания текущих координат</p>
     * @param robot модель робота
     */
    public RobotVisualizer(Robot robot) {
        this.robot = robot;
    }

    /**
     * <p>Рисует робота на входящем графическом контексте в входящих координатах и по определенному направлению</p>
     * @param graphics графический контекст
     */
    public void paint(Graphics2D graphics) {
        int robotCenterX = round(robot.getX());
        int robotCenterY = round(robot.getY());
        AffineTransform t = AffineTransform.getRotateInstance(robot.getDirection(), robotCenterX, robotCenterY); // поворачивает
        // робота на угол direction относительно координат robotCenterX, robotCenterY

        graphics.setTransform(t);
        graphics.setColor(Color.MAGENTA);
        fillOval(graphics, robotCenterX, robotCenterY, 30, 10);
        graphics.setColor(Color.BLACK);
        drawOval(graphics, robotCenterX, robotCenterY, 30, 10);
        graphics.setColor(Color.WHITE);
        fillOval(graphics, robotCenterX  + 10, robotCenterY, 5, 5);
        graphics.setColor(Color.BLACK);
        drawOval(graphics, robotCenterX  + 10, robotCenterY, 5, 5);
    }
}
