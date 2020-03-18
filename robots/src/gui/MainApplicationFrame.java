package gui;

import java.awt.*;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.*;

import log.Logger;


public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ResourceBundle localization;
    
    public MainApplicationFrame() {

        localization = getLocalization();

        // English version:
        //localization = ResourceBundle.getBundle("resources/LocalizationResources", new Locale("en", "US"));

        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow(localization);
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        setJMenuBar(new MenuBar(this, localization));

        // Confirm close window
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                Object[] options = { localization.getString("closeWindowYes"), localization.getString("closeWindowNo") };
                if (JOptionPane.showOptionDialog((Component) e.getSource(),
                        localization.getString("closeWindowQuestion"), localization.getString("closeWindowTitle"),
                        0,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, null) == 0)
                {
                    System.exit(0);
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    
    protected ResourceBundle getLocalization()
    {
        Locale currentLocale = Locale.getDefault();
        ResourceBundle resources;
        try
        {
            resources = ResourceBundle.getBundle("resources/LocalizationResources", currentLocale);
        }
        catch (MissingResourceException e)
        {
            resources = ResourceBundle.getBundle("resources/LocalizationResources", new Locale("en", "US"));
        }
        return resources;
    }


    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), localization);
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(localization.getString("protocolWorking"));
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }


}
