package gui;

import localization.LocalizationManager;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    public GameWindow(LocalizationManager localizationManager)
    {
        super(null, true, true, true, true);
        localizationManager.bindField("gameWindow.title", this::setTitle);

        getContentPane().add(new GameVisualizer(), BorderLayout.CENTER);
        pack();
    }
}
