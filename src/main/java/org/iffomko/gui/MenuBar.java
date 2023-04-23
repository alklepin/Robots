package org.iffomko.gui;

import org.iffomko.log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;

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
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "org.iffomko.gui.localizationProperties.menuBar.MenuBarResource",
                new Locale(System.getProperty("user.language"), System.getProperty("user.country"))
        );

        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = generateMenu(
                resourceBundle.getString("displayMode"),
                KeyEvent.VK_V,
                resourceBundle.getString("displayTypeDescription")
        );

        lookAndFeelMenu.add(generateMenuItem(resourceBundle.getString("systemScheme"), KeyEvent.VK_S, (event) -> {
            frame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            frame.invalidate();
        }));
        lookAndFeelMenu.add(generateMenuItem(resourceBundle.getString("universalScheme"), KeyEvent.VK_S, (event) -> {
            frame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            frame.invalidate();
        }));

        JMenu testMenu = generateMenu(
                resourceBundle.getString("tests"), KeyEvent.VK_T,
                resourceBundle.getString("testsDescription")
        );

        testMenu.add(generateMenuItem(
                resourceBundle.getString("messageInLog"),
                KeyEvent.VK_S,
                (event) -> Logger.debug(resourceBundle.getString("newString"))
        ));

        JMenu closeMenu = generateMenu(
                resourceBundle.getString("close"),
                KeyEvent.VK_X,
                resourceBundle.getString("closeDescription")
        );

        closeMenu.add(generateMenuItem(resourceBundle.getString("exit"), KeyEvent.VK_X, (event) -> {
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
