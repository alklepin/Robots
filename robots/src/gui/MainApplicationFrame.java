package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.*;

import log.Logger;

import static gui.Constants.GameVisualizerConstants.*;
import static gui.Constants.MainApplicationFrameConstants.*;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(SCREEN_OFFSET, SCREEN_OFFSET,
                screenSize.width - SCREEN_OFFSET * 2,
                screenSize.height - SCREEN_OFFSET * 2);

        setContentPane(desktopPane);
        Robot robot = new Robot(ROBOT_INITIAL_X_COORDINATE, ROBOT_INITIAL_Y_COORDINATE, ROBOT_INITIAL_DIRECTION);
        Target target = new Target(TARGET_INITIAL_X_COORDINATE, TARGET_INITIAL_Y_COORDINATE);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow(robot, target);
        gameWindow.setSize(INITIAL_GAME_WINDOW_WIDTH, INITIAL_GAME_WINDOW_HEIGHT);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(LOG_WINDOW_INITIAL_LOCATION_X, LOG_WINDOW_INITIAL_LOCATION_Y);
        logWindow.setSize(LOG_WINDOW_INITIAL_WIDTH, LOG_WINDOW_INITIAL_HEIGHT);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(LOGGER_INITIAL_MESSAGE);
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitButton());

        return menuBar;
    }

    private JMenuItem createExitButton() {
        JMenuItem exit = new JMenuItem(EXIT_BUTTON_TEXT);
        exit.addActionListener(event -> confirmExit());
        return exit;
    }

    private void confirmExit() {
        UIManager.put(YES_BUTTON_TEXT_CONSTANT, YES_BUTTON_TEXT);
        UIManager.put(NO_BUTTON_TEXT_CONSTANT, NO_BUTTON_TEXT);
        if (JOptionPane.showConfirmDialog(null, EXIT_CONFIRM_DIALOG_TEXT, EXIT_CONFIRM_DIALOG_TITLE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu(TEST_MENU_TEXT);

        testMenu.setMnemonic(KeyEvent.VK_T);

        JMenuItem addLogMessageItem = new JMenuItem(TEST_LOG_OPTION_TEXT, KeyEvent.VK_S);
        addLogMessageItem.addActionListener(event -> Logger.debug(TEST_LOG_TEXT));
        testMenu.add(addLogMessageItem);


        return testMenu;
    }
    public static void updateStateRobot(boolean isRobotMoving) {
        if (isRobotMoving) {
            Logger.debug(ROBOT_START_MOVING);
        } else {
            Logger.debug(ROBOT_STOP_MOVING);
        }
    }

    private JMenu createLookAndFeelMenu() {
        JMenu styleMenu = new JMenu(STYLE_MENU_TEXT);

        styleMenu.setMnemonic(KeyEvent.VK_V);

        styleMenu.add(createSystemStyleItem());
        styleMenu.add(createCrossPlatformStyleItem());

        return styleMenu;
    }

    private JMenuItem createCrossPlatformStyleItem() {
        JMenuItem crossPlatformStyle = new JMenuItem(CROSS_PLATFORM_STYLE_TEXT, KeyEvent.VK_S);
        crossPlatformStyle.addActionListener(event -> {
            setStyle(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return crossPlatformStyle;
    }

    private JMenuItem createSystemStyleItem() {
        JMenuItem systemStyle = new JMenuItem(SYSTEM_STYLE_TEXT, KeyEvent.VK_S);
        systemStyle.addActionListener(event -> {
            setStyle(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return systemStyle;
    }

    private void setStyle(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
