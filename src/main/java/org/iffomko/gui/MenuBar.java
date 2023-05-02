package org.iffomko.gui;

import org.iffomko.gui.localization.Localization;
import org.iffomko.log.Logger;
import org.iffomko.messagedFormatCached.MessageFormatCached;

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

        ResourceBundle resourceBundle = Localization.getInstance().getResourceBundle(packet);

        for (int i = 0; i < INSTANCE.getMenuCount(); i++) {
            JMenu menu = INSTANCE.getMenu(i);

            switch (menu.getAccessibleContext().getAccessibleName()) {
                case "displayMode": {
                    menu.setText(MessageFormatCached.format(resourceBundle.getString("displayMode")));
                    menu.getAccessibleContext().setAccessibleDescription(
                            MessageFormatCached.format(resourceBundle.getString("displayTypeDescription"))
                    );

                    for (int j = 0; j < menu.getItemCount(); j++) {
                        JMenuItem menuItem = menu.getItem(j);
                        String menuItemName = menuItem.getAccessibleContext().getAccessibleName();

                        if (menuItemName.equals("systemScheme")) {
                            menuItem.setText(MessageFormatCached.format(resourceBundle.getString("systemScheme")));
                        } else if (menuItemName.equals("universalScheme")) {
                            menuItem.setText(MessageFormatCached.format(resourceBundle.getString("universalScheme")));
                        }
                    }

                    break;
                }
                case "tests": {
                    menu.setText(MessageFormatCached.format(resourceBundle.getString("tests")));
                    menu.getAccessibleContext().setAccessibleDescription(MessageFormatCached.format(
                            resourceBundle.getString("testsDescription")
                    ));

                    for (int j = 0; j < menu.getItemCount(); j++) {
                        JMenuItem menuItem = menu.getItem(j);
                        String menuItemName = menuItem.getAccessibleContext().getAccessibleName();

                        if (menuItemName.equals("messageInLog")) {
                            menuItem.setText(MessageFormatCached.format(resourceBundle.getString("messageInLog")));
                        }
                    }

                    break;
                }
                case "close": {
                    menu.setText(MessageFormatCached.format(resourceBundle.getString("close")));
                    menu.getAccessibleContext().setAccessibleDescription(MessageFormatCached.format(
                            resourceBundle.getString("closeDescription")
                    ));

                    for (int j = 0; j < menu.getItemCount(); j++) {
                        JMenuItem menuItem = menu.getItem(j);
                        String menuItemName = menuItem.getAccessibleContext().getAccessibleName();

                        if (menuItemName.equals("exit")) {
                            menuItem.setText(MessageFormatCached.format(resourceBundle.getString("exit")));
                        }
                    }

                    break;
                }

                case "changeLanguageTitle": {
                    menu.setText(MessageFormatCached.format(resourceBundle.getString("changeLanguageTitle")));
                    menu.getAccessibleContext().setAccessibleDescription(MessageFormatCached.format(
                            resourceBundle.getString("changeLanguageDescription")
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

        ResourceBundle resourceBundle = Localization.getInstance().getResourceBundle(packet);

        MenuBar menuBar = new MenuBar();

        JMenu lookAndFeelMenu = generateMenu(
                MessageFormatCached.format(resourceBundle.getString("displayMode")),
                "displayMode",
                KeyEvent.VK_V,
                MessageFormatCached.format(resourceBundle.getString("displayTypeDescription"))
        );

        lookAndFeelMenu.add(generateMenuItem(
                MessageFormatCached.format(resourceBundle.getString("systemScheme")),
                "systemScheme",
                KeyEvent.VK_S, (event) -> {
                    frame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    frame.invalidate();
                }
        ));
        lookAndFeelMenu.add(generateMenuItem(
                MessageFormatCached.format(resourceBundle.getString("universalScheme")),
                "universalScheme",
                KeyEvent.VK_S, (event) -> {
                    frame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    frame.invalidate();
                }
        ));

        JMenu testMenu = generateMenu(
                MessageFormatCached.format(resourceBundle.getString("tests")),
                "tests",
                KeyEvent.VK_T,
                MessageFormatCached.format(resourceBundle.getString("testsDescription"))
        );

        testMenu.add(generateMenuItem(
                MessageFormatCached.format(resourceBundle.getString("messageInLog")),
                "messageInLog",
                KeyEvent.VK_S,
                (event) -> Logger.debug("New string")
        ));

        JMenu closeMenu = generateMenu(
                MessageFormatCached.format(resourceBundle.getString("close")),
                "close",
                KeyEvent.VK_X,
                MessageFormatCached.format(resourceBundle.getString("closeDescription"))
        );

        closeMenu.add(generateMenuItem(
                MessageFormatCached.format(resourceBundle.getString("exit")),
                "exit",
                KeyEvent.VK_X,
                (event) -> {
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                            new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)
                    );
                }
        ));

        JMenu changeLanguage = generateMenu(
                MessageFormatCached.format(resourceBundle.getString("changeLanguageTitle")),
                "changeLanguageTitle",
                KeyEvent.VK_B,
                MessageFormatCached.format(resourceBundle.getString("changeLanguageDescription"))
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
