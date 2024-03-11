package gui;

import log.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

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
        //menuBar.add(createSettingMenu());
        menuBar.add(createExitButton());
        return menuBar;
    }

//    private static JMenu createSettingMenu() {
//        ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.getDefault());
//        JMenu settingMenu = new JMenu("Настройки");
//        JMenu settingLanguage = new JMenu(rb.getString("Language"));
//        Locale ru = new Locale("ru", "RU");
//        Locale en = new Locale("en", "US");
//
//        {
//            JMenuItem english = new JMenuItem("Английский", KeyEvent.VK_S);
//            english.addActionListener((event) -> {
//                Locale.setDefault(new Locale("en", "US"));
//            });
//            settingLanguage.add(english);
//        }
//
//        {
//            JMenuItem russian = new JMenuItem("Русский");
//
//            settingLanguage.add(russian);
//        }
//
//        settingMenu.add(settingLanguage);
//        return settingMenu;
//    }

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
