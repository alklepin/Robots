package robots.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import robots.domain.game.Game;
import robots.domain.windows.InternalWindowJsonConfigurable;

public class GameWindow extends InternalWindowJsonConfigurable
{
    private String saveConfigPath = "gameWindow/config.json";
    private final GameVisualizer m_visualizer;
    public GameWindow(Game game)
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer(game);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        this.setSize(400, 400);
    }

    public String getSavePath()
    {
        return this.saveConfigPath;
    }

    public void load() {};

}
