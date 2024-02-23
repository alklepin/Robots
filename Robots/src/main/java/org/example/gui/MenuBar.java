package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MenuBar {
    static MainApplicationFrame mainFrame; // Используем вместо this, т.к. this имеет ввиду MainApplicationFrame объект
    // определяем MenuBar передавая ему аргумент MainApplicationFrame
    public MenuBar(MainApplicationFrame mainFrame){
        this.mainFrame = mainFrame;
    }
    public static JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitButton());
        return menuBar;
    }

    private static JMenu createLookAndFeelMenu() {

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                mainFrame.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                mainFrame.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        return lookAndFeelMenu;
    }


    private static JMenu createTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }

    private static JMenu createExitButton() {
        JMenu menu = new JMenu("Выйти");
        menu.setMnemonic(KeyEvent.VK_T);//KeyEvent.VK_T
        {
            JMenuItem exit = new JMenuItem("Выйти", KeyEvent.VK_S);
            exit.setFocusable(false);
            exit.addActionListener((event) -> {
                System.exit(0);
            });
            menu.add(exit);
        }

        {
            JMenuItem exit1 = new JMenuItem("Выйти", KeyEvent.VK_S);
            exit1.setFocusable(false);
            exit1.addActionListener((event) -> {
                System.exit(0);
            });
            menu.add(exit1);
        }
        return menu;
    }

    private static void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(mainFrame);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
    }
}
