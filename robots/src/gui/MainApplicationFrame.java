package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import log.Logger;

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
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeProgram();
            }
        });
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

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
//
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
//
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
//
//        return menuBar;
//    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(generateMappingMenu());
        menuBar.add(generateTestMenu());
        menuBar.add(generateOptionMenu());
        return menuBar;
    }

    private JMenu generateMappingMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext()
                .setAccessibleDescription("Управление режимом отображения приложения");
        lookAndFeelMenu.add(generateMenuItem("Системная схема", KeyEvent.VK_S, event -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            invalidate();
        }));
        lookAndFeelMenu.add(generateMenuItem("Универсальная схема", KeyEvent.VK_S, event -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            invalidate();
        }));
        return lookAndFeelMenu;
    }

    private JMenu generateTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
        testMenu.add(generateMenuItem("Сообщение в лог", KeyEvent.VK_S, event -> {
            Logger.debug("Новая строка");
        }));
        return testMenu;
    }

    private JMenu generateOptionMenu() {
        JMenu optionMenu = new JMenu("Дополнительно");
        optionMenu.setMnemonic(KeyEvent.VK_ESCAPE);
        optionMenu.getAccessibleContext().setAccessibleDescription("Закрыть");
        optionMenu.add(generateMenuItem("Выход", KeyEvent.VK_ESCAPE, event -> {
            closeProgram();
        }));
        return optionMenu;
    }


    private JMenuItem generateMenuItem(String name, int keyEvent, ActionListener actionListener) {
        JMenuItem item = new JMenuItem(name, keyEvent);
        item.addActionListener(actionListener);
        return item;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    //Общая функция для выхода
    private void closeProgram() {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        int YesNoBox = JOptionPane.showConfirmDialog(this, "Вы уверены?",
                "Окно подтверждения",
                JOptionPane.YES_NO_OPTION);
        if (YesNoBox == 0) {
            System.exit(0);
        }
    }
}
