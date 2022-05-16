package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Locale;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import listeners.WindowListenerImpl;
import localization.LocalizationManager;
import log.Logger;
import serialization.WindowStorage;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private static final Locale LOCALE_RU = new Locale("ru");

    private static final int INSET = 50;

    private JInternalFrame logWindow, gameWindow;
    private WindowStorage windowStorage;

    public MainApplicationFrame(LocalizationManager localizationManager, WindowStorage windowStorage) {
        this(localizationManager);
        this.windowStorage = windowStorage;

        if (windowStorage != null && windowStorage.isRestored()) {
            windowStorage.restore(this.getClass().toString(), this);
            windowStorage.restore(logWindow.getClass().toString(), logWindow);
            windowStorage.restore(gameWindow.getClass().toString(), gameWindow);
        } else {
            setExtendedState(MAXIMIZED_BOTH);
            pack();
        }
    }
    
    public MainApplicationFrame(LocalizationManager localizationManager) {
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(INSET, INSET, screenSize.width - INSET * 2, screenSize.height - INSET * 2);

        setContentPane(new JDesktopPane());
        setJMenuBar(new MenuGenerator(this).generateMenuBar(localizationManager));

        logWindow = createLogWindow(localizationManager);
        addWindow(logWindow);
        setMinimumSize(logWindow.getSize());
        Logger.debug("Протокол работает");

        gameWindow = new GameWindow(localizationManager);
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListenerImpl(localizationManager));
    }
    
    protected LogWindow createLogWindow(LocalizationManager localizationManager)
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), localizationManager);
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        add(frame).setVisible(true);
    }
}