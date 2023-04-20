package gui;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import static gui.Constants.RobotsProgramConstants.METAL_MENU_STYLE;
import static gui.Constants.RobotsProgramConstants.NIMBUS_MENU_STYLE;

public class RobotsProgram {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(NIMBUS_MENU_STYLE);
//        UIManager.setLookAndFeel(METAL_MENU_STYLE);
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
