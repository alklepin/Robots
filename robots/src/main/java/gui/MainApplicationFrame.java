package gui;

import gui.language.AppLanguage;
import gui.language.LanguageManager;
import log.Logger;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.*;


public class MainApplicationFrame extends JFrame {
    private final LanguageManager languageManager = new LanguageManager(Locale.getDefault().getLanguage());
    private final JDesktopPane desktopPane = new JDesktopPane();
    private ArrayList<JInternalFrame> internalFrames = new ArrayList<>();

    public MainApplicationFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);
        logWindow.setName("logWindow");
        internalFrames.add(logWindow);

        GameWindow gameWindow = new GameWindow(languageManager.getLocaleValue("gameWindow.title"));
        gameWindow.setSize(400, 400);
        gameWindow.setName("gameWindow");
        addWindow(gameWindow);
        internalFrames.add(gameWindow);

        generateAndSetMenuBar();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        WindowAdapter windowAdapter = new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                exit();
            }
        };
        addWindowListener(windowAdapter);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), languageManager.getLocaleValue("logWindow.title"));
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(languageManager.getLocaleValue("tests.startLog"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuItem createMenuItem(String description_key, int eventKey, ActionListener handler) {
        String description = languageManager.getLocaleValue(description_key);
        JMenuItem menuItem = new JMenuItem(description, eventKey);
        menuItem.addActionListener(handler);
        return menuItem;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu(languageManager.getLocaleValue("displayMode"));
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                languageManager.getLocaleValue("displayMode.description")
        );

        for (DisplayMode mode : DisplayMode.values()) {
            String key = String.format("displayMode.%s", mode.toString());
            ActionListener handler = (event) -> {
                setLookAndFeel(mode.className);
                this.invalidate();
            };
            menu.add(createMenuItem(key, KeyEvent.VK_S, handler));
        }
        return menu;
    }

    private JMenu createTestMenu() {
        JMenu menu = new JMenu(languageManager.getLocaleValue("tests"));
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(languageManager.getLocaleValue("tests.description"));

        ActionListener handler = (event) -> {
            Logger.debug(languageManager.getLocaleValue("tests.newLog"));
        };
        menu.add(createMenuItem("tests.logCommand", KeyEvent.VK_S, handler));
        return menu;
    }

    private JMenu createLanguageMenu() {
        JMenu menu = new JMenu(languageManager.getLocaleValue("language"));
        menu.setMnemonic(KeyEvent.VK_L);

        for (AppLanguage lang : AppLanguage.values()) {
            String key = String.format("language.%s", lang.locale);
            ActionListener handler = (event) -> {
                updateLocale(lang);
                this.revalidate();
            };
            menu.add(createMenuItem(key, KeyEvent.VK_K, handler));
        }
        return menu;
    }

    private void exit() {
        if (JOptionPane.showConfirmDialog(
                desktopPane,
                languageManager.getLocaleValue("close.confirmMessage"),
                languageManager.getLocaleValue("close.confirmTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE) == 0) {
            System.exit(0);
        }
    }

    private JMenu createExit() {
        JMenu createExit = new JMenu(languageManager.getLocaleValue("close"));
        JButton exit = new JButton(languageManager.getLocaleValue("close.button"));

        exit.addActionListener(event -> exit());
        createExit.add(exit);
        return  createExit;
    }

    private void generateAndSetMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createLanguageMenu());
        menuBar.add(createExit());
        setJMenuBar(menuBar);
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    private void updateLocale(AppLanguage language) {
        languageManager.changeLanguage(language);
        generateAndSetMenuBar();
        for (JInternalFrame frame : internalFrames) {
            frame.setTitle(languageManager.getLocaleValue(String.format("%s.title", frame.getName())));
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
}
