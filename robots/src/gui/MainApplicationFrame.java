package gui;

import config.ConfigManager;
import localization.LocalizationManager;
import log.Logger;
import model.RobotController;
import model.RobotModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainApplicationFrame extends JFrame {
    private static final String LOG_WINDOW_NAME = "LogWindow";
    private static final String GAME_WINDOW_NAME = "GameWindow";
    private static final String COORD_WINDOW_NAME = "CoordWindow";

    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ConfigManager configManager = new ConfigManager();
    private final WindowStateManager windowStateManager = new WindowStateManager(configManager);
    private final LocalizationManager localization = LocalizationManager.getInstance();

    private final RobotModel robotModel;
    private final RobotController robotController;

    private LogWindow logWindow;
    private GameWindow gameWindow;
    private RobotCoordinatesWindow coordWindow;
    private MenuBarFactory menuBarFactory;

    public MainApplicationFrame(RobotModel model, RobotController controller) {
        this.robotModel = model;
        this.robotController = controller;

        configManager.load();
        windowStateManager.loadMainWindowState(this);
        setContentPane(desktopPane);

        createWindows();
        setupMenuBar();

        windowStateManager.loadInternalWindowState(logWindow, LOG_WINDOW_NAME);
        windowStateManager.loadInternalWindowState(gameWindow, GAME_WINDOW_NAME);
        windowStateManager.loadInternalWindowState(coordWindow, COORD_WINDOW_NAME);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        localization.addListener(this::refreshLanguage);
    }

    private void createWindows() {
        logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();

        gameWindow = new GameWindow(robotModel, robotController);
        gameWindow.setSize(400, 400);

        coordWindow = new RobotCoordinatesWindow(robotModel);
        coordWindow.setSize(250, 100);
        coordWindow.setLocation(10, 400);

        addWindow(LOG_WINDOW_NAME, logWindow);
        addWindow(GAME_WINDOW_NAME, gameWindow);
        addWindow(COORD_WINDOW_NAME, coordWindow);
    }

    private void addWindow(String windowName, JInternalFrame frame) {
        windowStateManager.setupWindowBehavior(frame, windowName);
        desktopPane.add(frame);
    }

    private void setupMenuBar() {
        menuBarFactory = new MenuBarFactory(this);
        setJMenuBar(menuBarFactory.createMenuBar());
    }

    private void refreshLanguage() {
        logWindow.setTitle(localization.getString("window.log.title"));
        gameWindow.setTitle(localization.getString("window.game.title"));
        coordWindow.setTitle(localization.getString("window.coord.title"));
        setJMenuBar(menuBarFactory.createMenuBar());
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void exitApplication() {
        if (robotController != null) robotController.stopTimer();

        windowStateManager.saveMainWindowState(this);
        windowStateManager.saveInternalWindowState(logWindow, LOG_WINDOW_NAME);
        windowStateManager.saveInternalWindowState(gameWindow, GAME_WINDOW_NAME);
        windowStateManager.saveInternalWindowState(coordWindow, COORD_WINDOW_NAME);
        configManager.save();

        UIManager.put("OptionPane.yesButtonText", localization.getString("option.yes"));
        UIManager.put("OptionPane.noButtonText", localization.getString("option.no"));

        int result = JOptionPane.showConfirmDialog(
                this,
                localization.getString("exit.confirm.message"),
                localization.getString("exit.confirm.title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) { Logger.error("Fail in setLookAndFeel"); }
    }

    public void showWindowByName(String windowName) {
        JInternalFrame target = null;
        if (LOG_WINDOW_NAME.equals(windowName)) target = logWindow;
        else if (GAME_WINDOW_NAME.equals(windowName)) target = gameWindow;
        else if (COORD_WINDOW_NAME.equals(windowName)) target = coordWindow;
        if (target != null) {
            target.setVisible(true);
            try {
                if (target.isIcon()) target.setIcon(false);
            } catch (java.beans.PropertyVetoException e) { Logger.debug("PropertyVetoException");}
            target.toFront();
        }
    }
}
