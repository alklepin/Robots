package gui;

import model.RobotController;
import model.RobotModel;
import javax.swing.*;
import java.awt.*;

public class RobotsProgram {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            RobotModel model = new RobotModel();
            RobotController controller = new RobotController(model);
            MainApplicationFrame frame = new MainApplicationFrame(model, controller);
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
