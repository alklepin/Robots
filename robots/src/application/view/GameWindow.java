package application.view;


import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrame
{
    private final GameView gameView;

    public GameWindow(GameView gameView)
    {
        super("Игровое поле", true, true, true, true);
        this.gameView = gameView;
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.gameView, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(400, 400);
    }

    public GameView getGameView() {
        return this.gameView;
    }
}
