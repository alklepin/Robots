package main.java.gui;

import main.java.model.Model;
import main.java.view.GameView;
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
        ViewModel viewModel = new ViewModel(gameModel, gameWindow);


        viewModel = new ViewModel(gameModel, gameWindow);

        ViewModel finalViewModel = viewModel;
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame(finalViewModel);
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
