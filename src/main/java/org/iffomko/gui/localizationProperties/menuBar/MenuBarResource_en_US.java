package org.iffomko.gui.localizationProperties.menuBar;

import java.util.ListResourceBundle;

/**
 * <p>Класс, который отвечает за локализацию компоненты MenuBar для английского языка/p>
 */
public class MenuBarResource_en_US extends ListResourceBundle {
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
                {"displayMode", "Display mode"},
                {"displayTypeDescription", "Managing the display mode"},
                {"systemScheme", "System scheme"},
                {"universalScheme", "Universal scheme"},
                {"tests", "Tests"},
                {"testsDescription", "Test commands"},
                {"messageInLog", "Message to the log"},
                {"close", "Close"},
                {"closeDescription", "Closes the application"},
                {"exit", "Exit"},
                {"changeLanguageTitle", "Change UI language"},
                {"changeLanguageDescription", "Changing UI language"}
        };
    }
}
