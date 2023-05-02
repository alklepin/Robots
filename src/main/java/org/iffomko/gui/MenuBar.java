package org.iffomko.gui;

import org.iffomko.gui.localization.Localization;
import org.iffomko.log.Logger;
import org.iffomko.messagedFormatCached.MessageFormatting;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * Меню для класса MainApplicationFrame
 */
public class MenuBar extends JMenuBar implements Observer {
    private static volatile MenuBar INSTANCE;
    private static final Object synchronizationObject = new Object();

    /**
     * Локализирует нужные поля этой компоненты
     */
    private void setupLocalization() {
        String packet = "org.iffomko.gui.localizationProperties.menuBar.MenuBarResource";
        MessageFormatting messageFormatting = MessageFormatting.getInstance();

        for (int i = 0; i < INSTANCE.getMenuCount(); i++) {
            JMenu menu = INSTANCE.getMenu(i);

            switch (menu.getAccessibleContext().getAccessibleName()) {
                case "displayMode": {
                    menu.setText(messageFormatting.format("displayMode", packet));
                    menu.getAccessibleContext().setAccessibleDescription(
                            messageFormatting.format("displayTypeDescription", packet)
                    );

                    for (int j = 0; j < menu.getItemCount(); j++) {
                        JMenuItem menuItem = menu.getItem(j);
                        String menuItemName = menuItem.getAccessibleContext().getAccessibleName();

                        if (menuItemName.equals("systemScheme")) {
                            menuItem.setText(messageFormatting.format("systemScheme", packet));
                        } else if (menuItemName.equals("universalScheme")) {
                            menuItem.setText(messageFormatting.format("universalScheme", packet));
                        }
                    }

                    break;
                }
                case "tests": {
                    menu.setText(messageFormatting.format("tests", packet));
                    menu.getAccessibleContext().setAccessibleDescription(messageFormatting.format(
                            "testsDescription", packet
                    ));

                    for (int j = 0; j < menu.getItemCount(); j++) {
                        JMenuItem menuItem = menu.getItem(j);
                        String menuItemName = menuItem.getAccessibleContext().getAccessibleName();

                        if (menuItemName.equals("messageInLog")) {
                            menuItem.setText(messageFormatting.format("messageInLog", packet));
                        }
                    }

                    break;
                }
                case "close": {
                    menu.setText(messageFormatting.format("close", packet));
                    menu.getAccessibleContext().setAccessibleDescription(messageFormatting.format(
                            "closeDescription", packet
                    ));

                    for (int j = 0; j < menu.getItemCount(); j++) {
                        JMenuItem menuItem = menu.getItem(j);
                        String menuItemName = menuItem.getAccessibleContext().getAccessibleName();

                        if (menuItemName.equals("exit")) {
                            menuItem.setText(messageFormatting.format("exit", packet));
                        }
                    }

                    break;
                }

                case "changeLanguageTitle": {
                    menu.setText(messageFormatting.format("changeLanguageTitle", packet));
                    menu.getAccessibleContext().setAccessibleDescription(messageFormatting.format(
                            "changeLanguageDescription", packet
                    ));
                    break;
                }
            }
        }
    }

    private MenuBar() {};

    /**
     * Возвращает меню
     * @return - меню
     */
    public static MenuBar getInstance(MainApplicationFrame mainApplicationFrame)
    {
        if (INSTANCE == null) {
            synchronized (synchronizationObject) {
                if (INSTANCE == null) {
                    INSTANCE = generateMenuBar(mainApplicationFrame);
                }
            }
        }

        return INSTANCE;
    }

    /**
     * Генерирует меню
     * @return - меню
     */
    private static MenuBar generateMenuBar(MainApplicationFrame frame) {
        String packet = "org.iffomko.gui.localizationProperties.menuBar.MenuBarResource";
        MessageFormatting messageFormatting = MessageFormatting.getInstance();

        MenuBar menuBar = new MenuBar();

        JMenu lookAndFeelMenu = generateMenu(
                messageFormatting.format("displayMode", packet),
                "displayMode",
                KeyEvent.VK_V,
                messageFormatting.format("displayTypeDescription", packet)
        );

        lookAndFeelMenu.add(generateMenuItem(
                messageFormatting.format("systemScheme", packet),
                "systemScheme",
                KeyEvent.VK_S, (event) -> {
                    frame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    frame.invalidate();
                }
        ));
        lookAndFeelMenu.add(generateMenuItem(
                messageFormatting.format("universalScheme", packet),
                "universalScheme",
                KeyEvent.VK_S, (event) -> {
                    frame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    frame.invalidate();
                }
        ));

        JMenu testMenu = generateMenu(
                messageFormatting.format("tests", packet),
                "tests",
                KeyEvent.VK_T,
                messageFormatting.format("testsDescription", packet)
        );

        testMenu.add(generateMenuItem(
                messageFormatting.format("messageInLog", packet),
                "messageInLog",
                KeyEvent.VK_S,
                (event) -> Logger.debug("New string")
        ));

        JMenu closeMenu = generateMenu(
                messageFormatting.format("close", packet),
                "close",
                KeyEvent.VK_X,
                messageFormatting.format("closeDescription", packet)
        );

        closeMenu.add(generateMenuItem(
                messageFormatting.format("exit", packet),
                "exit",
                KeyEvent.VK_X,
                (event) -> {
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                            new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)
                    );
                }
        ));

        JMenu changeLanguage = generateMenu(
                messageFormatting.format("changeLanguageTitle", packet),
                "changeLanguageTitle",
                KeyEvent.VK_B,
                messageFormatting.format("changeLanguageDescription", packet)
        );

        changeLanguage.add(generateMenuItem("Русский", "Русский", KeyEvent.VK_B, (event) -> {
            Localization.getInstance().setLocale(new Locale("ru", "RU"));
        }));

        changeLanguage.add(generateMenuItem("English", "English", KeyEvent.VK_B, (event) -> {
            Localization.getInstance().setLocale(new Locale("en", "US"));
        }));

        changeLanguage.add(generateMenuItem("Default", "Default", KeyEvent.VK_B, (event) -> {
            Localization.getInstance().setLocale(new Locale("", ""));
        }));

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(closeMenu);
        menuBar.add(changeLanguage);

        return menuBar;
    }

    /**
     * Генерирует меню
     * @param title - название меню
     * @param titleLabel - название меню, которое не меняется никогда
     * @param mnemonic - клавиша, с которой ассоциируется меню
     * @param descriptionText - описание меню
     * @return - готовое меню
     */
    private static JMenu generateMenu(String title, String titleLabel, int mnemonic, String descriptionText) {
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        menu.getAccessibleContext().setAccessibleName(titleLabel);
        menu.getAccessibleContext().setAccessibleDescription(descriptionText);

        return menu;
    }

    /**
     * Генерирует элемент меню
     * @param text - название элемента
     * @param keyEventNumber - номер клавиши, на который будет переключаться
     * @return - готовый элемент меню
     */
    private static JMenuItem generateMenuItem(String text, String name, int keyEventNumber, ActionListener listener) {
        JMenuItem item = new JMenuItem(text, keyEventNumber);
        item.getAccessibleContext().setAccessibleName(name);

        if (listener != null) {
            item.addActionListener(listener);
        }

        return item;
    }

    /**
     * <p>Этот метод вызывается каждый раз, когда наблюдаемый объект изменяется</p>
     * <p>
     *     В программе этот метод вызывается тогда, когда наблюдаемый объект, который наследуется от <code>Observable</code>,
     *     вызывает метод <code>notifyObservers</code>
     * </p>
     * @param o   - наблюдаемый объект, который уведомил об изменениях
     * @param arg - аргумент, который был положен при вызове метода <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if ((o instanceof Localization) && arg.equals(Localization.KEY_LOCAL_CHANGED)) {
            setupLocalization();
        }
    }
}
