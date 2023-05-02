package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import logic.IObjectState;
import logic.LocalConfig;
import org.json.simple.JSONObject;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import log.Logger;

public class MainApplicationFrame extends JFrame {

    private final JDesktopPane desktopPane = new JDesktopPane();
    RobotModel robotModel = new RobotModel();
    RobotController controller = new RobotController(robotModel, new RobotView());
    private final GameWindow gameWindow = new GameWindow(controller);
    private final LogWindow logWindow = createLogWindow();

    CoordinatesWindow coordWindow = new CoordinatesWindow();

    private final IObjectState configManager = new LocalConfig();

    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        setJMenuBar(generateMenuBar());

        addWindow(coordWindow, 300, 300);
        robotModel.addObserver(coordWindow);

        addWindow(logWindow, 200, 200);
        addWindow(gameWindow, 400, 400);
        applyConfig();

        saveConfiguration();
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

            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);

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
            public void menuDeselected(MenuEvent e) {}

            @Override
            public void menuCanceled(MenuEvent e) {}
        });

        return exitMenu;
    }


    public void exitProgram() {
        String[] options = new String[2];
        options[0] = "Да";
        options[1] = "Нет";
        if (JOptionPane.showOptionDialog(this.getContentPane(),
                "Вы уверены, что хотите выйти?", "Закрыть окно?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                null) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    protected void saveConfiguration() {
        JSONObject json = new JSONObject();
        saveConfigurationOfElement("gameWindow", json, gameWindow);
        saveConfigurationOfElement("logWindow", json, logWindow);
        saveConfigurationOfElement("CoordinatesWindow", json, coordWindow);
        configManager.save(json);
    }

    protected void saveConfigurationOfElement(String name, JSONObject json, JInternalFrame window) {
        json.put(name+ "X", window.getX());
        json.put(name+ "Y", window.getY());
        json.put(name + "Width", window.getWidth());
        json.put(name+ "Height", window.getHeight());
    }

    protected void applyConfig() {
        JSONObject config = configManager.load();
        if (config == null) {
            return;
        }
        gameWindow.setSize(((Long) config.get("gameWindowWidth")).intValue(), ((Long) config.get("gameWindowHeight")).intValue());
        logWindow.setSize(((Long) config.get("logWindowWidth")).intValue(), ((Long) config.get("logWindowHeight")).intValue());
        coordWindow.setSize(((Long) config.get("CoordinatesWindowWidth")).intValue(), ((Long) config.get("CoordinatesWindowHeight")).intValue());
        gameWindow.setLocation(((Long) config.get("gameWindowX")).intValue(), ((Long) config.get("gameWindowY")).intValue());
        logWindow.setLocation(((Long) config.get("logWindowX")).intValue(), ((Long) config.get("logWindowY")).intValue());
        coordWindow.setLocation(((Long) config.get("CoordinatesWindowX")).intValue(), ((Long) config.get("CoordinatesWindowY")).intValue());
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
