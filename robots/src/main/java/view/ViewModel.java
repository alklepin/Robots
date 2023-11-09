package main.java.view;

import main.java.gui.GameWindow;
import main.java.model.Model;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

public class ViewModel {
    private final Model gameModel;
    private final GameWindow gameWindow;
    private final java.util.Timer timer = initTimer();

    private static java.util.Timer initTimer() {
        return new Timer("events generator", true);
    }

    public ViewModel(Model gameModel, GameWindow gameWindow) {
        this.gameModel = gameModel;
        this.gameWindow = gameWindow;
        initListeners();
    }

    private void initListeners() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameModel.setDimension(gameWindow.getSize());
                getGameView().updateView();
            }
        }, 0, 5);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameModel.updateModel();
            }
        }, 0, 5);
        gameWindow.getGameView().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameModel.setTarget(e.getPoint());
                getGameView().repaint();
            }
        });
        gameWindow.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                super.componentResized(e);
                System.out.println("resize");
                gameModel.setDimension((gameWindow.getSize()));
                System.out.println(gameModel.getDimension());
            }
        });

        gameWindow.getGameView().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getPoint());
                gameModel.setTarget(e.getPoint());
                getGameView().repaint();
            }
        });
    }

    public GameView getGameView() {
        return gameWindow.getGameView();
    }

    public GameWindow getGameWindow() {
        return gameWindow;
    }
}