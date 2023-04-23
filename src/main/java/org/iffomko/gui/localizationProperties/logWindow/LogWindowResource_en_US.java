package org.iffomko.gui.localizationProperties.logWindow;

import java.util.ListResourceBundle;

/**
 * <p>Класс, который отвечает за локализацию компоненты <code>LogWindow</code> для английского языка/p>
 */
public class LogWindowResource_en_US extends ListResourceBundle {
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
                {"logWindowTitle", "Work protocol"}
        };
    }
}
