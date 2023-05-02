package org.iffomko.gui.localizationProperties.menuBar;

import java.util.ListResourceBundle;

/**
 * <p>Класс, который отвечает за стандартную локализацию в виде транслита для компоненты <code>MenuBar</code></p>
 */
public class MenuBarResource extends ListResourceBundle {
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
                {"displayMode", "Rezhim otobrazheniya"},
                {"displayTypeDescription", "Upravlenie rezhimom otobrazheniya prilozheniya"},
                {"systemScheme", "Sistemnaya shema"},
                {"universalScheme", "Universalnaya shema"},
                {"tests", "Testi"},
                {"testsDescription", "Testovie comandi"},
                {"messageInLog", "Soobshenie v log"},
                {"close", "Zakrit"},
                {"closeDescription", "Zaktrivaet prilozhenie"},
                {"exit", "Vihod"},
                {"changeLanguageTitle", "Pomenat' yazik interfeysa"},
                {"changeLanguageDescription", "Menayet yazik interfeysa"}
        };
    }
}
