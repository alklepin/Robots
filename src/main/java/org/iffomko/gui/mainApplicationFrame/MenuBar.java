package org.iffomko.gui.mainApplicationFrame;

import org.iffomko.gui.mainApplicationFrame.MainApplicationFrame;
import org.iffomko.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

/**
 * Меню для класса MainApplicationFrame
 */
public class MenuBar {
    private static JMenuBar menuBar = null;
    private static MainApplicationFrame frame = null;

    private MenuBar() {};

    /**
     * Возвращает меню
     * @return - меню
     */
    public static JMenuBar getMenu(MainApplicationFrame mainApplicationFrame)
    {
        if (menuBar == null) {
            frame = mainApplicationFrame;
            menuBar = generateMenu();
        }

        return menuBar;
    }

    /**
     * Генерирует меню
     * @return - меню
     */
    private static JMenuBar generateMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = generateMenu(
                "Режим отображения",
                KeyEvent.VK_V,
                "Управление режимом отображения приложения"
        );

        lookAndFeelMenu.add(generateMenuItem("Системная схема", KeyEvent.VK_S, (event) -> {
            frame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            frame.invalidate();
        }));
        lookAndFeelMenu.add(generateMenuItem("Универсальная схема", KeyEvent.VK_S, (event) -> {
            frame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            frame.invalidate();
        }));

        JMenu testMenu = generateMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");

        testMenu.add(generateMenuItem("Сообщение в лог", KeyEvent.VK_S, (event) -> Logger.debug("Новая строка")));

        JMenu closeMenu = generateMenu("Закрыть", KeyEvent.VK_X, "Закрывает приложение");

        closeMenu.add(generateMenuItem("Выход", KeyEvent.VK_X, (event) -> {
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)
            );
        }));

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
    private static JMenuItem generateMenuItem(String text, int keyEventNumber, ActionListener listener) {
        JMenuItem item = new JMenuItem(text, keyEventNumber);

        item.addActionListener(listener);

        return item;
    }
}
