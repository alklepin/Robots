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

        setContentPane(desktopPane);;

        logWindow = createLogWindow();
        addWindow(logWindow);

        gameWindow = createGameWindow();
        addWindow(gameWindow);

        if (windowConfigsArePresent() && userWantsToRestoreWindows())
            loadWindowStates();

        setJMenuBar(new MenuBar(this, localization, config));

        // Confirm close window
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                Object[] options = {localization.getString("yes"), localization.getString("no")};
                if (JOptionPane.showOptionDialog((Component) e.getSource(),
                        localization.getString("closeWindowQuestion"), localization.getString("closeWindowTitle"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, null) == 0) {
                    saveWindowStates();
                    System.exit(0);
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private boolean userWantsToRestoreWindows()
    {
        Object[] options = {localization.getString("yes"), localization.getString("no")};
        return JOptionPane.showOptionDialog(this,
                localization.getString("restoreWindowsQuestion"),
                localization.getString("restoreWindowsTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, null) == 0;
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

    private void saveWindowStates() {
        for (var frame: desktopPane.getAllFrames())
            saveWindowState(frame, frame.getClass().getName());
        saveConfig();
    }

    private void saveWindowState(JInternalFrame window, String name) {
        if (!window.isClosed()) {
            config.setProperty(name + "Minimized", String.valueOf(window.isIcon()));
            config.setProperty(name + "PositionX", String.valueOf(window.getX()));
            config.setProperty(name + "PositionY", String.valueOf(window.getY()));
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

    private boolean windowConfigsArePresent() {
        for (var frame: desktopPane.getAllFrames()) {
            if (config.containsKey(frame.getClass().getName() + "Minimized"))
                return true;
        }
        return false;
    }

    private void loadWindowStates()
    {
        for (var frame: desktopPane.getAllFrames()) {
            var name = frame.getClass().getName();
            if (config.containsKey(name + "Minimized"))
                loadWindowConfig(frame, name);
        }
    }

    protected void loadWindowConfig(JInternalFrame window, String name) {
        var isMinimized = Boolean.parseBoolean(config.getProperty(name + "Minimized"));
        var x = Integer.parseInt(config.getProperty(name + "PositionX"));
        var y = Integer.parseInt(config.getProperty(name + "PositionY"));
        try {
            window.setIcon(isMinimized);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        window.setLocation(x, y);
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
        logWindow.pack();
        Logger.debug(localization.getString("protocolWorking"));
        return logWindow;
    }

    protected GameWindow createGameWindow() {
        gameWindow = new GameWindow(localization);
        gameWindow.setSize(400, 400);
        return gameWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}
