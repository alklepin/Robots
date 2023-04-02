package ru.kemichi.robots.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.WindowEvent;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ru.kemichi.robots.log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = generateMenu("Режим отображения",
                KeyEvent.VK_V,
                "Управление режимом отображения приложения"
        );
        JMenuItem system = generateMenuItem("Системная схема",
                KeyEvent.VK_S,
                "system");
        JMenuItem universal = generateMenuItem("Системная схема",
                KeyEvent.VK_U,
                "universal");
        lookAndFeelMenu.add(system);
        lookAndFeelMenu.add(universal);

        JMenu testMenu = generateMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");
        JMenuItem logTest = generateMenuItem("Сообщение в лог", KeyEvent.VK_L, "log");
        testMenu.add(logTest);

        JMenu actionsMenu = generateMenu("Действия", KeyEvent.VK_A, "Дополнительные действия");
        JMenuItem exit = generateMenuItem("Закрыть", KeyEvent.VK_Q, "quit");
        actionsMenu.add(exit);

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(actionsMenu);

        return menuBar;
    }

    private JMenu generateMenu(String menuName, int menuHotkey, String menuDescription) {
        JMenu jMenu = new JMenu(menuName);
        jMenu.setMnemonic(menuHotkey);
        jMenu.getAccessibleContext().setAccessibleDescription(menuDescription);
        return jMenu;
    }

    private JMenuItem generateMenuItem(String menuItemName, int menuItemMnemonic, String description) {
        JMenuItem jMenuItem = new JMenuItem(menuItemName);
        jMenuItem.setMnemonic(menuItemMnemonic);
        jMenuItem.addActionListener(
                (event) -> {
                    switch (description) {
                        case "system" -> {
                            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            this.invalidate();
                        }
                        case "universal" -> {
                            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                            this.invalidate();
                        }
                        case "log" -> Logger.debug("Новая строка");
                        case "quit" -> exitConfirmation();
                    }
                }
        );
        return jMenuItem;
    }

    private void exitConfirmation() {
        Object[] choices = {"Да", "Нет"};
        Object defaultChoice = choices[0];
        int confirmed = JOptionPane.showOptionDialog(null,
                "Вы действительно хотите выйти?",
                "Выход из программы",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                defaultChoice);

        if (confirmed == JOptionPane.YES_OPTION) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
