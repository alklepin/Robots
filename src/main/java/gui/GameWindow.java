package gui;

import listeners.InternalFrameListenerImpl;
import localization.LocalizationManager;
import serialization.WindowStorage;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;

public class GameWindow extends JInternalFrame
{
    public GameWindow(LocalizationManager localizationManager, WindowStorage windowStorage)
    {
        super(null, true, true, true, true);
        localizationManager.bindField("gameWindow.title", this::setTitle);

        getContentPane().add(new GameVisualizer(), BorderLayout.CENTER);
        pack();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(new InternalFrameListenerImpl(localizationManager, windowStorage, this));

    }
}