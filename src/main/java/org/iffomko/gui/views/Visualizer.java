package org.iffomko.gui.views;

import java.awt.*;

/**
 * Абстрактный класс, который реализовывает интерфейс <code>IVisualizer</code> и хранит в себе базовые методы,
 * которые могут отрисовывать какие-то фигуры или помогать вычислить какое-то значение
 */
public abstract class Visualizer implements IVisualizer {
    /**
     * Рисует овал без контура, только внутренняя часть
     * @param g - компонента, где надо отрисовать сам овал
     * @param centerX - координата оси OX, вокруг которой надо отрисовать овал
     * @param centerY - координата оси OY, вокруг которой надо отрисовать овал
     * @param diam1 - первый диаметр овала
     * @param diam2 - второй диаметр овала
     */
    protected static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Рисует контур овала
     * @param g - компонента, где надо отрисовать контур овала
     * @param centerX - координата оси OX, в которой надо отрисовать овал
     * @param centerY - координата оси OY, в которой надо отрисовать овал
     * @param diam1 - первый диаметр овала
     * @param diam2 - второй диаметр овала
     */
    protected static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    /**
     * Округляет значение в большую сторону
     * @param value - входное значение
     * @return - округленное значение
     */
    protected static int round(double value)
    {
        return (int)(value + 0.5);
    }
}
