package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import localization.LocalizationManager;
import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private static final Locale LOCALE_RU = new Locale("ru");

    private final JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame(LocalizationManager localizationManager) {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow(localizationManager);
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow(localizationManager);
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        MenuGenerator menuGenerator = new MenuGenerator(this);
        setJMenuBar(menuGenerator.generateMenuBar(localizationManager));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        desktopPane.add(frame);
        frame.setVisible(true);
    }
}