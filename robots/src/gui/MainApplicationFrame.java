package gui;


import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import log.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {

    private final JDesktopPane desktopPane = new JDesktopPane();
    RobotModel robotModel = new RobotModel();
    RobotView robotView = new RobotView();
    RobotController controller = new RobotController(robotModel, robotView);
    private final GameWindow gameWindow = new GameWindow(controller);
    private final LogWindow logWindow = createLogWindow();

    RobotCoordinates coordinatesWindow = new RobotCoordinates();
    public MainApplicationFrame() {
//Make the big window be indented 50 pixels from each edge
//of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        addWindow(logWindow, 200, 200);
        addWindow(gameWindow, 400, 400);
        addWindow(coordinatesWindow, 300, 300);
        robotModel.addObserver(coordinatesWindow);
        applyConfig();

        saveConfiguration();
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                saveConfiguration();
                exitProgram();
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

    protected void addWindow(JInternalFrame frame, int height, int width) {
        frame.setSize(width, height);
        desktopPane.add(frame);
        frame.setVisible(true);
    }

// protected JMenuBar createMenuBar() {
// JMenuBar menuBar = new JMenuBar();
//
// //Set up the lone menu.
// JMenu menu = new JMenu("Document");
// menu.setMnemonic(KeyEvent.VK_D);
// menuBar.add(menu);
//
// //Set up the first menu item.
// JMenuItem menuItem = new JMenuItem("New");
// menuItem.setMnemonic(KeyEvent.VK_N);
// menuItem.setAccelerator(KeyStroke.getKeyStroke(
// KeyEvent.VK_N, ActionEvent.ALT_MASK));
// menuItem.setActionCommand("new");
//// menuItem.addActionListener(this);
// menu.add(menuItem);
//
// //Set up the second menu item.
// menuItem = new JMenuItem("Quit");
// menuItem.setMnemonic(KeyEvent.VK_Q);
// menuItem.setAccelerator(KeyStroke.getKeyStroke(
// KeyEvent.VK_Q, ActionEvent.ALT_MASK));
// menuItem.setActionCommand("quit");
//// menuItem.addActionListener(this);
// menu.add(menuItem);
//
// return menuBar;
// }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitMenu());

        return menuBar;
    }


    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().

                setAccessibleDescription(
                        "Управление режимом отображения приложения");


        lookAndFeelMenu.add(
                createJMenuItem("Системная схема",
                        KeyEvent.VK_S,
                        UIManager.getSystemLookAndFeelClassName()));


        lookAndFeelMenu.add(createJMenuItem("Универсальная схема",
                KeyEvent.VK_S,
                UIManager.getCrossPlatformLookAndFeelClassName()));

        return lookAndFeelMenu;

    }

    private JMenuItem createJMenuItem(String name, int key, String action) {
        JMenuItem item = new JMenuItem(name, key);
        item.addActionListener((event) -> {
            setLookAndFeel(action);
            this.invalidate();
        });
        return item;
    }

    private JMenu createTestMenu() {
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


    private JMenu createExitMenu() {
        JMenu exitMenu = new JMenu("Выход");
        exitMenu.setMnemonic(KeyEvent.VK_E);
        exitMenu.getAccessibleContext().setAccessibleDescription(
                "Выход из приложения");


        exitMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                exitProgram();
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        return exitMenu;
    }


    public void exitProgram() {
        int reply = JOptionPane.showConfirmDialog(null,
                "Вы уверены что хотите выйти?", "Robots ", JOptionPane.YES_NO_OPTION);
        int yesNoOption = JOptionPane.YES_NO_OPTION;

        if (reply == JOptionPane.YES_OPTION) {

            System.exit(0);

        }
    }
    //локализация переменных
    {UIManager.put("OptionPane.yesButtonText","Да");
        UIManager.put("OptionPane.noButtonText","Нет");}

    protected void saveConfiguration() {
        JSONObject json = new JSONObject();
        json.put("gameWindowX", gameWindow.getX());
        json.put("gameWindowY", gameWindow.getY());
        json.put("gameWindowWidth", gameWindow.getWidth());
        json.put("gameWindowHeight", gameWindow.getHeight());

        json.put("logWindowX", logWindow.getX());
        json.put("logWindowY", logWindow.getY());
        json.put("logWindowWidth", logWindow.getWidth());
        json.put("logWindowHeight", logWindow.getHeight());

        json.put("RobotCoordinatesX", coordinatesWindow.getX());
        json.put("RobotCoordinatesY", coordinatesWindow.getY());
        json.put("RobotCoordinatesWidth", coordinatesWindow.getWidth());
        json.put("RobotCoordinatesHeight", coordinatesWindow.getHeight());
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
        coordinatesWindow.setSize(((Long) config.get("RobotCoordinatesWidth")).intValue(), ((Long) config.get("RobotCoordinatesHeight")).intValue());
        gameWindow.setLocation(((Long) config.get("gameWindowX")).intValue(), ((Long) config.get("gameWindowY")).intValue());
        logWindow.setLocation(((Long) config.get("logWindowX")).intValue(), ((Long) config.get("logWindowY")).intValue());
        coordinatesWindow.setLocation(((Long) config.get("RobotCoordinatesX")).intValue(), ((Long) config.get("RobotCoordinatesY")).intValue());
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
