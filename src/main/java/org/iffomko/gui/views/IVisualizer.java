package org.iffomko.gui.views;

import java.awt.*;

/**
 * Этот интерфейс фиксирует контракт для всех классов, которые умеют от рисовывать какие-то модели
 */
public interface IVisualizer {
    /**
     * Метод, который от рисовывает модель на графическом контексте
     * @param graphics графический контекст
     */
    void paint(Graphics2D graphics);
}
