package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;

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
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        setContentPane(desktopPane);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int result = JOptionPane.showConfirmDialog(MainApplicationFrame.this, "Закрыть приложение?", "Окно подтверждения", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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

    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();

        Menu lookAndFeelMenu = new Menu("Режим отображения",
                "Управление режимом отображения приложения",
                KeyEvent.VK_V);

        lookAndFeelMenu.createMenu("Системная схема",
                (event) -> {
                    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();
                }, KeyEvent.VK_S);

        lookAndFeelMenu.createMenu("Универсальная схема", (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        }, KeyEvent.VK_S);

        Menu testMenu = new Menu("Тесты", "Тестовые команды", KeyEvent.VK_T);

        testMenu.createMenu("Сообщение в лог", (event) -> {
            Logger.debug("Новая строка");
        }, KeyEvent.VK_S);

        Menu quitMenu = new Menu("Выход", "Выход из приложения", KeyEvent.VK_Q);

        quitMenu.createMenu("Закрыть приложение", (event) -> {
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }, KeyEvent.VK_X);

        menuBar.add(lookAndFeelMenu.getMenu());
        menuBar.add(testMenu.getMenu());
        menuBar.add(quitMenu.getMenu());
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
                | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
