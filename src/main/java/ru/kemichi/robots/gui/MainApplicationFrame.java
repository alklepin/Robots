package ru.kemichi.robots.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ru.kemichi.robots.log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ResourceBundle bundle;

    public MainApplicationFrame(ResourceBundle defaultBundle) {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        bundle = defaultBundle;
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow(bundle);
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), bundle);
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(bundle.getString("protocolOK"));
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu lookAndFeelMenu = generateMenu(bundle.getString("lookAndFeelMenu"),
                KeyEvent.VK_V,
                bundle.getString("lookAndFeelMenuDescription")
        );
        JMenuItem system = generateMenuItem(bundle.getString("systemItem"),
                KeyEvent.VK_S,
                "system");
        JMenuItem universal = generateMenuItem(bundle.getString("universalItem"),
                KeyEvent.VK_U,
                "universal");
        lookAndFeelMenu.add(system);
        lookAndFeelMenu.add(universal);

        JMenu testMenu = generateMenu(bundle.getString("testMenu"),
                KeyEvent.VK_T,
                bundle.getString("testMenuDescription"));
        JMenuItem logTest = generateMenuItem(bundle.getString("logItem"), KeyEvent.VK_L, "log");
        testMenu.add(logTest);

        JMenu actionsMenu = generateMenu(bundle.getString("actionsMenu"),
                KeyEvent.VK_A,
                bundle.getString("actionsMenuDescription"));
        JMenuItem quit = generateMenuItem(bundle.getString("quitItem"), KeyEvent.VK_Q, "quit");
        actionsMenu.add(quit);

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        menuBar.add(actionsMenu);

        return menuBar;
    }

    private JMenu generateMenu(String menuName, int menuHotkey, String menuDescription) {
        JMenu jMenu = new JMenu(menuName);
        jMenu.setMnemonic(menuHotkey);
        jMenu.getAccessibleContext().setAccessibleDescription(menuDescription);
        return jMenu;
    }

    private JMenuItem generateMenuItem(String menuItemName, int menuItemMnemonic, String description) {
        JMenuItem jMenuItem = new JMenuItem(menuItemName);
        jMenuItem.setMnemonic(menuItemMnemonic);
        jMenuItem.addActionListener(
                (event) -> {
                    switch (description) {
                        case "system" -> {
                            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            this.invalidate();
                        }
                        case "universal" -> {
                            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                            this.invalidate();
                        }
                        case "log" -> Logger.debug(bundle.getString("logDefaultMessage"));
                        case "quit" -> exitConfirmation();
                    }
                }
        );
        return jMenuItem;
    }

    private void exitConfirmation() {
        Object[] choices = {bundle.getString("yes"), bundle.getString("no")};
        Object defaultChoice = choices[0];
        int confirmed = JOptionPane.showOptionDialog(null,
                bundle.getString("quitQuestion"),
                bundle.getString("quitTitle"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                choices,
                defaultChoice);

        if (confirmed == JOptionPane.YES_OPTION) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
