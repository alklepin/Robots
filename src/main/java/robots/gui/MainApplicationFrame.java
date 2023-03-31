package robots.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import robots.log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    private final GameWindow gameWindow;

    private final LogWindow logWindow;

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);


        logWindow = createLogWindow();
        addWindow(logWindow);


        gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        applyConfig();

        saveConfiguration();
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                saveConfiguration();
                gameWindow.dispose();
                logWindow.dispose();
                System.exit(0);
            }
        });
    }

    protected void saveConfiguration() {
        JSONObject json = new JSONObject();
        json.put("gameWindowX", gameWindow.getX());
        json.put("gameWindowY", gameWindow.getY());
        json.put("gameWindowWidth", gameWindow.getWidth());
        json.put("gameWindowHeight", gameWindow.getHeight());
        json.put("gameWindowisIcon", gameWindow.isIcon());


        json.put("logWindowX", logWindow.getX());
        json.put("logWindowY", logWindow.getY());
        json.put("logWindowWidth", logWindow.getWidth());
        json.put("logWindowHeight", logWindow.getHeight());
        json.put("logWindowisIcon", logWindow.isIcon());


        // Save JSON to user home directory
        String userHomeDir = System.getProperty("user.home");
        File robotsDir = new File(userHomeDir + "/.Robots");
        if (!robotsDir.exists()) {
            robotsDir.mkdir();
        }
        File configFile = new File(userHomeDir + "/.Robots/config.json");
        try (FileWriter fileWriter = new FileWriter(configFile.getAbsolutePath())) {
            fileWriter.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void applyConfig() {
        JSONObject config = readFromConfig();
        if (config == null) {
            return;
        }
        gameWindow.setSize(((Long) config.get("gameWindowWidth")).intValue(), ((Long) config.get("gameWindowHeight")).intValue());
        logWindow.setSize(((Long) config.get("logWindowWidth")).intValue(), ((Long) config.get("logWindowHeight")).intValue());
        gameWindow.setLocation(((Long) config.get("gameWindowX")).intValue(), ((Long) config.get("gameWindowY")).intValue());
        logWindow.setLocation(((Long) config.get("logWindowX")).intValue(), ((Long) config.get("logWindowY")).intValue());
        if ((Boolean) config.get("gameWindowisIcon")) {
            try {
                gameWindow.setIcon(true);
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
        }
        if ((Boolean) config.get("logWindowisIcon")) {
            try {
                logWindow.setIcon(true);
            } catch (java.beans.PropertyVetoException e) {
                e.printStackTrace();
            }
        }

    }

    protected JSONObject readFromConfig() {

        File configFile = new File(System.getProperty("user.home") + "/.Robots/config.json");
        JSONParser parser = new JSONParser();
        if (configFile.exists()) {
            try {
                return (JSONObject) parser.parse(new FileReader(configFile));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
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

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
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
}