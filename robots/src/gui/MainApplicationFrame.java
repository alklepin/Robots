package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JOptionPane;

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

    private final LogWindow logWindowGlobal;
    private final GameWindow gameWindowGlobal;
    private static final String WINDOW_STATE_FILE = System.getProperty("user.home") + "\\window_state.properties";

    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(this,
                "Вы действительно хотите выйти?",
                "Подтверждение выхода",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            saveWindowStates();
            dispose();
            System.exit(0);
        }
    }

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        
        LogWindow logWindow = createLogWindow();
        logWindowGlobal = logWindow;
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindowGlobal = gameWindow;
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        loadWindowStates();

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                confirmExit();
            }
        });
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

    private void saveWindowStates() {
        Properties props = new Properties();

        props.setProperty("frame.x", String.valueOf(getX()));
        props.setProperty("frame.y", String.valueOf(getY()));
        props.setProperty("frame.width", String.valueOf(getWidth()));
        props.setProperty("frame.height", String.valueOf(getHeight()));
        props.setProperty("frame.extendedState", String.valueOf(getExtendedState()));

        saveInternalFrameState(props, logWindowGlobal, "LogWindow");
        saveInternalFrameState(props, gameWindowGlobal, "GameWindow");

        try (FileOutputStream out = new FileOutputStream(WINDOW_STATE_FILE)) {
            props.store(out, "Window states");
        } catch (IOException e) {
            System.err.println("Не удалось сохранить состояние окон: " + e.getMessage());
        }
    }

    private void saveInternalFrameState(Properties props, JInternalFrame frame, String prefix) {
        props.setProperty(prefix + ".x", String.valueOf(frame.getX()));
        props.setProperty(prefix + ".y", String.valueOf(frame.getY()));
        props.setProperty(prefix + ".width", String.valueOf(frame.getWidth()));
        props.setProperty(prefix + ".height", String.valueOf(frame.getHeight()));
        props.setProperty(prefix + ".isIcon", String.valueOf(frame.isIcon()));
        props.setProperty(prefix + ".isMaximum", String.valueOf(frame.isMaximum()));
    }

    private void loadWindowStates() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(WINDOW_STATE_FILE)) {
            props.load(in);
        } catch (IOException e) {
            return;
        }

        try {
            int x = Integer.parseInt(props.getProperty("frame.x"));
            int y = Integer.parseInt(props.getProperty("frame.y"));
            int width = Integer.parseInt(props.getProperty("frame.width"));
            int height = Integer.parseInt(props.getProperty("frame.height"));
            setBounds(x, y, width, height);
            int extendedState = Integer.parseInt(props.getProperty("frame.extendedState"));
            setExtendedState(extendedState);
        } catch (NumberFormatException e) {}

        loadInternalFrameState(props, logWindowGlobal, "LogWindow");
        loadInternalFrameState(props, gameWindowGlobal, "GameWindow");
    }


    private void loadInternalFrameState(Properties props, JInternalFrame frame, String prefix) {
        try {
            int x = Integer.parseInt(props.getProperty(prefix + ".x"));
            int y = Integer.parseInt(props.getProperty(prefix + ".y"));
            int width = Integer.parseInt(props.getProperty(prefix + ".width"));
            int height = Integer.parseInt(props.getProperty(prefix + ".height"));
            boolean isIcon = Boolean.parseBoolean(props.getProperty(prefix + ".isIcon"));
            boolean isMaximum = Boolean.parseBoolean(props.getProperty(prefix + ".isMaximum"));

            // Сначала устанавливаем границы, потом развёрнутость/свёрнутость
            frame.setBounds(x, y, width, height);
            if (isMaximum) {
                frame.setMaximum(true);
            } else {
                frame.setIcon(isIcon);
            }
        } catch (NumberFormatException | PropertyVetoException e) {}
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

    private void addSystemLookAndFeel(JMenu lookAndFeelMenu)
    {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
    }

    private void addCrossplatformLookAndFeel(JMenu lookAndFeelMenu)
    {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
    }

    private void addLogMessageItem(JMenu testMenu)
    {
        JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            Logger.debug("Новая строка");
        });
        testMenu.add(addLogMessageItem);
    }

    private void addExitLookAndFeel(JMenu exitMenu)
    {
        JMenuItem addLogMessageItem = new JMenuItem("Завершение работы программы", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
            confirmExit();
        });
        exitMenu.add(addLogMessageItem);
    }

    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        addSystemLookAndFeel(lookAndFeelMenu);

        addCrossplatformLookAndFeel(lookAndFeelMenu);

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        addLogMessageItem(testMenu);

        JMenu exitMenu = new JMenu("Выход");
        exitMenu.setMnemonic(KeyEvent.VK_T);
        exitMenu.getAccessibleContext().setAccessibleDescription(
                "Меню выхода");

        addExitLookAndFeel(exitMenu);

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(exitMenu);
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
