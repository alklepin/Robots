package main.java.gui;

import java.awt.BorderLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import main.java.view.GameView;

public class GameWindow extends JInternalFrame {
    private final GameView gameView;

    public GameWindow(GameView gameView) {
        super("Игровое поле", true, true, true, true);
        this.gameView = gameView;
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.gameView, "Center");
        this.getContentPane().add(panel);
        this.pack();
    }

    public GameView getGameView() {
        return this.gameView;
    }
}