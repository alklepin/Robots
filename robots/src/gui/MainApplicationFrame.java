package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;


public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ResourceBundle localization;
    private final Properties config;
    private final String configPath = "resources/config.properties";
    private final String localizationPath = "resources/LocalizationResources";
    private LogWindow logWindow;
    private GameWindow gameWindow;

    public MainApplicationFrame() {
        config = getConfiguration();
        localization = getLocalization();

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        logWindow = createLogWindow();
        addWindow(logWindow);

        gameWindow = createGameWindow();
        addWindow(gameWindow);

        setJMenuBar(new MenuBar(this, localization, config));

        // Confirm close window
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                Object[] options = {localization.getString("closeWindowYes"), localization.getString("closeWindowNo")};
                if (JOptionPane.showOptionDialog((Component) e.getSource(),
                        localization.getString("closeWindowQuestion"), localization.getString("closeWindowTitle"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, null) == 0) {
                    saveWindowsState();
                    System.exit(0);
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private Properties getConfiguration() {

        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream(configPath);
            property.load(fis);
        } catch (IOException e) {
            System.err.println("Error! Config file not found");
            System.exit(5);
        }

        return property;
    }

    private void saveWindowsState() {
        saveWindowState(logWindow, "log");
        saveWindowState(gameWindow, "game");
        saveConfig();
    }

    private void saveWindowState(JInternalFrame window, String name) {
        if (!window.isClosed()) {
            config.setProperty(name + "WindowMinimized", String.valueOf(window.isIcon()));
            config.setProperty(name + "WindowPositionX", String.valueOf(window.getX()));
            config.setProperty(name + "WindowPositionY", String.valueOf(window.getY()));
        }
    }

    private void saveConfig()
    {
        try {
            config.store(new FileOutputStream(configPath), null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    protected ResourceBundle getLocalization()
    {
        ResourceBundle resources;
        resources = ResourceBundle.getBundle(localizationPath,
                new Locale(config.getProperty("lang"), config.getProperty("country")));

        return resources;
    }


    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), localization);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        loadWindowConfig(logWindow, "log");
        logWindow.pack();
        Logger.debug(localization.getString("protocolWorking"));
        return logWindow;
    }

    protected GameWindow createGameWindow() {
        gameWindow = new GameWindow(localization);
        gameWindow.setSize(400, 400);
        loadWindowConfig(gameWindow, "game");
        return gameWindow;
    }

    protected void loadWindowConfig(JInternalFrame window, String name) {
        if (config.containsKey(name + "WindowMinimized")) {
            var isMinimized = Boolean.parseBoolean(config.getProperty(name + "WindowMinimized"));
            var x = Integer.parseInt(config.getProperty(name + "WindowPositionX"));
            var y = Integer.parseInt(config.getProperty(name + "WindowPositionY"));
            try {
                window.setIcon(isMinimized);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
            window.setLocation(x, y);
        }
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}
