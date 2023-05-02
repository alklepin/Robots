package org.iffomko.gui;

import org.iffomko.log.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Меню для класса MainApplicationFrame
 */
public class MenuBar {
    private static JMenuBar menuBar = null;
    private static MainApplicationFrame frame = null;
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle(
            "org.iffomko.gui.localizationProperties.menuBar.MenuBarResource",
            new Locale(System.getProperty("user.language"), System.getProperty("user.country"))
    );

    private MenuBar() {};

    /**
     * <p>Обработчик события при нажатии на кнопку, которая позволяет выбрать .jar файл с настройками робота</p>
     */
    private static void onLoadRobot() {
        UIManager.put("FileChooser.cancelButtonText", resourceBundle.getString("FileChooser.cancelButtonText"));
        UIManager.put("FileChooser.fileNameLabelText", resourceBundle.getString("FileChooser.fileNameLabelText"));
        UIManager.put("FileChooser.filesOfTypeLabelText", resourceBundle.getString("FileChooser.filesOfTypeLabelText"));
        UIManager.put("FileChooser.lookInLabelText", resourceBundle.getString("FileChooser.lookInLabelText"));
        UIManager.put("FileChooser.folderNameLabelText", resourceBundle.getString("FileChooser.folderNameLabelText"));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(resourceBundle.getString("selectRobotJarTitle"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                return f.getName().endsWith(".jar");
            }

            @Override
            public String getDescription() {
                return resourceBundle.getString("selectRobotJarDescription");
            }
        });

        int result = fileChooser.showDialog(frame, resourceBundle.getString("selectRobotJarApproveBtn"));

        if (
                result == JFileChooser.APPROVE_OPTION &&
                fileChooser.getSelectedFile().getPath().toLowerCase().endsWith(".jar")
        ) {
            String robotPath = fileChooser.getSelectedFile().getPath();
            System.out.println("Все супер! Мы выбрали робота!\n" + robotPath);

            // ToDo: тут надо запустить загрузчик робота
        }
    }

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

        JMenu loadRobot = generateMenu(
                resourceBundle.getString("loadRobot"),
                KeyEvent.VK_L,
                resourceBundle.getString("loadRobotDescription")
        );

        loadRobot.add(generateMenuItem(resourceBundle.getString("loadRobotItem"), KeyEvent.VK_L, (event) -> {
            onLoadRobot();
        }));

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(closeMenu);
        menuBar.add(loadRobot);

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

        if (listener != null) {
            item.addActionListener(listener);
        }

        return item;
    }
}
