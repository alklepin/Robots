package robots.gui;

import robots.domain.InternalWindowJsonConfigurable;
import robots.domain.Robot;
import robots.log.LogLevel;
import robots.log.LogWindowSource;
import robots.log.Logger;
import robots.log.RobotPositionLogger;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Robot robot = new Robot();
        LogWindowSource logSource = new LogWindowSource(50);
        RobotPositionLogger robotLogger = new RobotPositionLogger(logSource);
        robot.addObserver(robotLogger);
        GameWindow gameWindow = new GameWindow(robot);
        LogWindow logWindow = new LogWindow(logSource);

        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame(
                    logSource,
                    gameWindow,
                    logWindow
            );
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}