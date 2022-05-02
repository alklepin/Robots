package gui;

import listeners.MenuListenerImpl;
import localization.LocalizationManager;
import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Locale;

public class MenuGenerator {

    private final MainApplicationFrame mainApplicationFrame;
    private static final Locale LOCALE_RU = new Locale("ru");

    public MenuGenerator(MainApplicationFrame _mainApplicationFrame) {
        mainApplicationFrame = _mainApplicationFrame;
    }

    public JMenuBar generateMenuBar(LocalizationManager localizationManager)
    {
        JMenuBar menuBar = new JMenuBar();

        var lookAndFeelMenu = new MenuBuilder(localizationManager)
                .setText("lookAndFeelMenu.text")
                .setMnemonic(KeyEvent.VK_V)
                .addMenuItem("lookAndFeelMenu.system", KeyEvent.VK_S,
                        e -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName()))
                .addMenuItem("lookAndFeelMenu.universal", KeyEvent.VK_S,
                        e -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()))
                .build();
        menuBar.add(lookAndFeelMenu);

        var testMenu = new MenuBuilder(localizationManager)
                .setText("testMenu.text")
                .setMnemonic(KeyEvent.VK_S)
                .setDescription("testMenu.description")
                .addMenuItem("testMenu.sendMessage", KeyEvent.VK_S, e -> Logger.debug("Новая строка"))
                .build();
        menuBar.add(testMenu);

        var languageMenu = new MenuBuilder(localizationManager)
                .setText("languageMenu.text")
                .addMenuItem("languageMenu.english", KeyEvent.VK_E,
                        e -> localizationManager.changeLocale(Locale.ENGLISH))
                .addMenuItem("languageMenu.russian", KeyEvent.VK_R,
                        e -> localizationManager.changeLocale(LOCALE_RU))
                .build();
        menuBar.add(languageMenu);

        var exitMenu = new MenuBuilder(localizationManager)
                .setText("exitAppMenu.text")
                .addMListener(new MenuListenerImpl(localizationManager))
                .build();
        menuBar.add(exitMenu);

        return menuBar;
    }

//    private JMenu generateLookAndFeelMenu(LocalizationManager localizationManager) {
//        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
//        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
//        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
//                "Управление режимом отображения приложения");
//
//        addScheme(lookAndFeelMenu, "Системная схема", UIManager.getSystemLookAndFeelClassName());
//        addScheme(lookAndFeelMenu, "Универсальная схема", UIManager.getCrossPlatformLookAndFeelClassName());
//        return lookAndFeelMenu;
//    }

//    private JMenu generateTestMenu(LocalizationManager localizationManager) {
//        JMenu testMenu = new JMenu("Тесты");
//        testMenu.setMnemonic(KeyEvent.VK_T);
//        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
//        addTestField(testMenu, "Сообщение в лог", "Новая строка");
//        return testMenu;
//    }

//    private void addTestField(JMenu testMenu, String name, String message) {
//        JMenuItem addLogMessageItem = new JMenuItem(name, KeyEvent.VK_S);
//        addLogMessageItem.addActionListener((event) -> Logger.debug(message));
//        testMenu.add(addLogMessageItem);
//    }
//
//    private void addScheme(JMenu lookAndFeelMenu, String theme, String systemLookAndFeelClassName) {
//        JMenuItem systemLookAndFeel = new JMenuItem(theme, KeyEvent.VK_S);
//        systemLookAndFeel.addActionListener((event) -> {
//            setLookAndFeel(systemLookAndFeelClassName);
//            mainApplicationFrame.invalidate();
//        });
//        lookAndFeelMenu.add(systemLookAndFeel);
//    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(mainApplicationFrame);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
        }
    }
}
