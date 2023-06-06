package robots.gui;

import robots.domain.game.Game;
import robots.domain.game.Robot;
import robots.domain.game.Target;
import robots.log.LogWindowSource;
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
        Target target = new Target(150, 150);
        Robot robot = new Robot(150, 150);
        target.addObserver(robot);
        Game game = new Game(
                robot,
                target
        );
        LogWindowSource logSource = new LogWindowSource(50);
        RobotPositionLogger robotLogger = new RobotPositionLogger(logSource);
        robot.addObserver(robotLogger);
        GameWindow gameWindow = new GameWindow(game);
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