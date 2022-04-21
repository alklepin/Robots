package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MenuGenerator {

    private final MainApplicationFrame mainApplicationFrame;

    public MenuGenerator(MainApplicationFrame _mainApplicationFrame) {
        mainApplicationFrame = _mainApplicationFrame;
    }

    public JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(generateLookAndFeelMenu());
        menuBar.add(generateTestMenu());
        return menuBar;
    }

    private JMenu generateLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        addScheme(lookAndFeelMenu, "Системная схема", UIManager.getSystemLookAndFeelClassName());
        addScheme(lookAndFeelMenu, "Универсальная схема", UIManager.getCrossPlatformLookAndFeelClassName());
        return lookAndFeelMenu;
    }

    private JMenu generateTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
        addTestField(testMenu, "Сообщение в лог", "Новая строка");
        return testMenu;
    }

    private void addTestField(JMenu testMenu, String name, String message) {
        JMenuItem addLogMessageItem = new JMenuItem(name, KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> Logger.debug(message));
        testMenu.add(addLogMessageItem);
    }

    private void addScheme(JMenu lookAndFeelMenu, String theme, String systemLookAndFeelClassName) {
        JMenuItem systemLookAndFeel = new JMenuItem(theme, KeyEvent.VK_S);
        systemLookAndFeel.addActionListener((event) -> {
            setLookAndFeel(systemLookAndFeelClassName);
            mainApplicationFrame.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(mainApplicationFrame);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
        }
    }
}
