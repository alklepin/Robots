package gui;

import log.Logger;
import save.Memorizable;
import save.StateManager;
import save.WindowInitException;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JInternalFrame implements Memorizable
{
    private final String attribute = "gameWindow";
    private final GameVisualizer m_visualizer;
    private final StateManager stateManager;

    public GameWindow(StateManager stateManager)
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        this.stateManager = stateManager;
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        try {
            dememorize();
        } catch (WindowInitException e) {
            setSize(400, 400);
            Logger.debug(e.getMessage());
        }
    }

    @Override
    public void memorize() {
        stateManager.storeFrame(attribute, this);
    }

    @Override
    public void dememorize() throws WindowInitException {
        stateManager.recoverFrame(attribute, this);
    }
}
