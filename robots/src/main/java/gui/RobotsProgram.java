package main.java.gui;

import model.RobotModel;
import model.TargetModel;
import main.java.view.GameView;
import model.Model;
import main.java.view.ViewModel;
import javax.swing.*;
import java.awt.*;

public class RobotsProgram {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Model gameModel = new Model();
        GameView gameView = new GameView(gameModel);
        GameWindow gameWindow = new GameWindow(gameView);


        TargetModel targetModel = new TargetModel();
        RobotModel robotModel = new RobotModel(targetModel);

        GameVisualizer gameVisualizer = new GameVisualizer(robotModel, targetModel);
        PositionWindow positionWindow = new PositionWindow(robotModel, 800, 600);
        gameVisualizer.onRedrawEvent();
        positionWindow.updateCoords();

        ViewModel viewModel = new ViewModel(gameModel, gameWindow);

        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame(viewModel);
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
