package org.iffomko.gui.localizationProperties.robotPositionPanel;

import java.util.ListResourceBundle;

/**
 * <p>Класс, который отвечает за локализацию компоненты <code>RobotPositionPanel</code>> для русского языка/p>
 */
public class RobotPositionPanelResource_ru_RU extends ListResourceBundle {
    /**
     * <p>
     *     Возвращает массив пар ключ, значение, где ключ обязан быть <code>String</code>, а значение это любой тип, который
     *     наследуется от <code>Object</code>. Ключ хранится на 0 индексе, а значение на 1.
     * </p>
     *
     * @return массив типа <code>Object</code>, который представляет собой массив пар ключ-значение
     */
    @Override
    protected Object[][] getContents() {
        return new Object[][] {
                {"direction", "Направление"}
        };
    }
}
