package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import static gui.Constants.GameWindowConstants.GAME_WINDOW_TITLE;

public class GameWindow extends JInternalFrame {

    public GameWindow(Robot robot, Target target) {
        super(GAME_WINDOW_TITLE, true, true, true, true);
        GameController controller = new GameController(robot, target);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(controller, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
