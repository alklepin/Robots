package org.robotsteam.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import org.robotsteam.log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int inset = 50;
        setBounds(inset, inset,
            screenSize.width - 2*inset,
            screenSize.height - 2*inset
        );

        setContentPane(desktopPane);

        addWindow(initLogWindow());
        addWindow(initGameWindow());

        setJMenuBar(new MenuBar(this));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected LogWindow initLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());

        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);

        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");

        return logWindow;
    }

    protected GameWindow initGameWindow() {
        GameWindow gameWindow = new GameWindow();

        gameWindow.setLocation(300, 100);
        gameWindow.setSize(400,  400);

        return gameWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}
