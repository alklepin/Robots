package main.java.gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import main.java.model.GameModel;
import main.java.model.Robot;
import main.java.view.GameView;
import main.java.view.ViewModel;

public class RobotsProgram {
    public RobotsProgram() {
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        GameModel gameModel = new GameModel();
        GameView gameView = new GameView(gameModel);
        GameWindow gameWindow = new GameWindow(gameView);
        Robot robot = new Robot();
        PositionWindow positionWindow = new PositionWindow(robot, 800, 600);
        positionWindow.updateCoords();
        ViewModel viewModel = new ViewModel(gameModel, gameWindow);
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame(viewModel);
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(6);
        });
    }
}
