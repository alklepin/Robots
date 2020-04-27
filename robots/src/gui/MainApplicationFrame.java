package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame() {
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        IMenuEvent schemeEvent = () -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        addMenu(schemeEvent, lookAndFeelMenu, "Системная схема");
        addMenu(schemeEvent, lookAndFeelMenu, "Универсальная схема");

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        IMenuEvent logEvent = () -> Logger.debug("Новая строка");

        addMenu(logEvent, testMenu, "Сообщение в лог");

        final JButton exitItem = new JButton("Выход");
        exitItem.addActionListener((event) -> {
            closeAction(this);
        });

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitItem);

        return menuBar;
    }
    
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException ignored)
        {
        }
    }

    private void addMenu(IMenuEvent e, JMenu menu, String name) {
        JMenuItem lookAndFeelItem = new JMenuItem(name, KeyEvent.VK_S);
        lookAndFeelItem.addActionListener((event) -> {
            e.func();
            this.invalidate();
        });
        menu.add(lookAndFeelItem);
    }

    private void closeAction(Window objOnClose) {
        int choice = JOptionPane.showConfirmDialog(objOnClose,
                "Вы точно хотите выйти?",
                "Окно подтверждения",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(objOnClose, WindowEvent.WINDOW_CLOSING));
        }
    }
}

interface IMenuEvent {
    void func();
}
