package org.iffomko.gui.localizationProperties.gameWindow;

import java.util.ListResourceBundle;

/**
 * <p>Класс, который отвечает за стандартную локализацию в виде транслита для компоненты <code>GameWindow</code></p>
 */
public class GameWindowResource extends ListResourceBundle {
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
                {"gameWindowTitle", "Igrovoe pole"}
        };
    }
}
