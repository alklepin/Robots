package main.java.view;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import main.java.gui.GameWindow;
import main.java.model.GameModel;

public class ViewModel {
    private final GameModel gameModel;
    private final GameWindow gameWindow;
    private final Timer timer = initTimer();

    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    public ViewModel(GameModel gameModel, GameWindow gameWindow) {
        this.gameModel = gameModel;
        this.gameWindow = gameWindow;
        this.initListeners();
    }

    private void initListeners() {
        this.timer.schedule(new TimerTask() {
            public void run() {
                ViewModel.this.gameModel.setDimension(ViewModel.this.gameWindow.getSize());
                ViewModel.this.getGameView().updateView();
            }
        }, 0L, 5L);
        this.timer.schedule(new TimerTask() {
            public void run() {
                ViewModel.this.gameModel.updateModel();
            }
        }, 0L, 5L);
        this.gameWindow.getGameView().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                ViewModel.this.gameModel.setTarget(e.getPoint());
                ViewModel.this.getGameView().repaint();
            }
        });
        this.gameWindow.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                System.out.println("resize");
                ViewModel.this.gameModel.setDimension(ViewModel.this.gameWindow.getSize());
                System.out.println(ViewModel.this.gameModel.getDimension());
            }
        });
        this.gameWindow.getGameView().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getPoint());
                ViewModel.this.gameModel.setTarget(e.getPoint());
                ViewModel.this.getGameView().repaint();
            }
        });
    }

    public GameView getGameView() {
        return this.gameWindow.getGameView();
    }

    public GameWindow getGameWindow() {
        return this.gameWindow;
    }
}
