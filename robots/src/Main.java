package application;

import application.view.GameView;
import application.view.GameWindow;
import application.model.GameModel;
import application.viewModel.ViewModel;

import javax.swing.*;
import java.awt.*;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        GameModel gameModel = new GameModel();
        GameView gameView = new GameView(gameModel);
        GameWindow gameWindow = new GameWindow(gameView);
        ViewModel viewModel = new ViewModel(gameModel, gameWindow);

        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame(viewModel);
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
