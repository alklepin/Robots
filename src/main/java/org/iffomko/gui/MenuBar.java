package org.iffomko.gui;

import org.iffomko.log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MenuBar {
    private static JMenuBar menuBar = null;

    private MenuBar() {};

    /**
     * Возвращает объект на меню
     * @return - меню
     */
    public static JMenuBar getMenu()
    {
        if (menuBar == null) {
            menuBar = generateMenu();
        }

        return menuBar;
    }

    /**
     * Генерирует меню со всеми разделами
     * @return - возвращает сгенерированное меню
     */
    private static JMenuBar generateMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = generateMenu(
                "Режим отображения",
                KeyEvent.VK_V,
                "Управление режимом отображения приложения"
        );

        lookAndFeelMenu.add(generateMenuItem("Системная схема", KeyEvent.VK_S));
        lookAndFeelMenu.add(generateMenuItem("Универсальная схема", KeyEvent.VK_S));

        JMenu testMenu = generateMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");

        testMenu.add(generateMenuItem("Сообщение в лог", KeyEvent.VK_S));

        JMenu closeMenu = generateMenu("Закрыть", KeyEvent.VK_X, "Закрывает приложение");

        closeMenu.add(generateMenuItem("Выход", KeyEvent.VK_X));

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(closeMenu);

        return menuBar;
    }

    /**
     * Генерирует меню
     * @param title - название меню
     * @param mnemonic - клавиша, с которой ассоциируется меню
     * @param descriptionText - описание меню
     * @return - готовое меню
     */
    private static JMenu generateMenu(String title, int mnemonic, String descriptionText) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        menu.getAccessibleContext().setAccessibleDescription(descriptionText);

        return menu;
    }

    /**
     * Генерирует элемент меню
     * @param text - название элемента
     * @param keyEventNumber - номер клавиши, на который будет переключаться
     * @return - готовый элемент меню
     */
    private static JMenuItem generateMenuItem(String text, int keyEventNumber) {
        return new JMenuItem(text, keyEventNumber);
    }
}
