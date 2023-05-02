package org.iffomko.gui.localizationProperties.menuBar;

import java.util.ListResourceBundle;

/**
 * <p>Класс, который отвечает за локализацию компоненты MenuBar для русского языка</p>
 */
public class MenuBarResource_ru_RU extends ListResourceBundle {
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
                {"displayMode", "Режим отображения"},
                {"displayTypeDescription", "Управление режимом отображения"},
                {"systemScheme", "Системная схема"},
                {"universalScheme", "Универсальная схема"},
                {"tests", "Тесты"},
                {"testsDescription", "Тестовые команды"},
                {"messageInLog", "Сообщение в лог"},
                {"newString", "Новая строка"},
                {"close", "Закрыть"},
                {"closeDescription", "Закрывает приложение"},
                {"exit", "Выход"},
                {"loadRobot", "Загрузить робота"},
                {"loadRobotDescription", "Позволяет указать путь до робота"},
                {"loadRobotItem", "Выбрать путь"},
                {"selectRobotJarTitle", "Выбор робота"},
                {"selectRobotJarDescription", "Только .jar файлы"},
                {"selectRobotJarApproveBtn", "Выбрать робота"},
                {"FileChooser.cancelButtonText", "Отмена"},
                {"FileChooser.fileNameLabelText", "Имя файла"},
                {"FileChooser.filesOfTypeLabelText", "Типы файлов"},
                {"FileChooser.lookInLabelText", "Директория"},
                {"FileChooser.folderNameLabelText", "Путь директории"}
        };
    }
}
