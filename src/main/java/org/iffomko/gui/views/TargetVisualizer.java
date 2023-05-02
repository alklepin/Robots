package org.iffomko.gui.views;

import org.iffomko.gui.models.Target;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * <p>Класс, который рисует цель, которую пытается достичь робот</p>
 */
public class TargetVisualizer extends Visualizer {
    private final Target target;

    /**
     * Инициализирует поле с моделью цели
     * @param target модель цели
     */
    public TargetVisualizer(Target target) {
        this.target = target;
    }

    /**
     * <p>Рисует курсор, к которому стремится робот</p>
     * @param graphics компонента, нужно отрисовать овал
     */
    public void paint(Graphics2D graphics)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); // поворачивает на ноль градусов указатель
        graphics.setTransform(t);
        graphics.setColor(Color.GREEN);
        fillOval(graphics, target.getX(), target.getY(), 5, 5);
        graphics.setColor(Color.BLACK);
        drawOval(graphics, target.getX(), target.getY(), 5, 5);
    }
}
