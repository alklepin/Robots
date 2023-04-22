package ru.kemichi.robots.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ru.kemichi.robots.gui.windows.AbstractWindow;
import ru.kemichi.robots.log.Logger;
import ru.kemichi.robots.utility.ConfigurationKeeper;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ResourceBundle bundle;

    public MainApplicationFrame(ResourceBundle defaultBundle, int inset, AbstractWindow[] windows) {
        bundle = defaultBundle;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);

        ConfigurationKeeper configurationKeeper = new ConfigurationKeeper();

        for (AbstractWindow window : windows) {
            window.defaultWindowSetup();
            addWindow(window);
            configurationKeeper.addNewConfigurableItem(window);
            configurationKeeper.applyConfiguration(window);
        }


        setJMenuBar(generateMenuBar());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                configurationKeeper.saveAllConfigurations();
                exitConfirmation();
            }
        });
    }


    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        addThemeMenu(menuBar);
        addTestMenu(menuBar);
        addActionsMenu(menuBar);

        return menuBar;
    }

    private void addThemeMenu(JMenuBar menuBar) {
        addMenu(
                menuBar,
                generateMenu(bundle.getString("lookAndFeelMenu"),
                        KeyEvent.VK_V,
                        bundle.getString("lookAndFeelMenuDescription")
                ),
                generateMenuItem(
                        bundle.getString("systemItem"),
                        KeyEvent.VK_S, (event) -> {
                            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            this.invalidate();
                        }),
                generateMenuItem(
                        bundle.getString("universalItem"),
                        KeyEvent.VK_U,
                        (event) -> {
                            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                            this.invalidate();
                        })
        );
    }

    private void addTestMenu(JMenuBar menuBar) {
        addMenu(
                menuBar,
                generateMenu(
                        bundle.getString("testMenu"),
                        KeyEvent.VK_T,
                        bundle.getString("testMenuDescription")
                ),
                generateMenuItem(
                        bundle.getString("logItem"),
                        KeyEvent.VK_L,
                        (event) -> Logger.debug(bundle.getString("logDefaultMessage"))
                )
        );
    }

    private void addActionsMenu(JMenuBar menuBar) {
        addMenu(
                menuBar,
                generateMenu(
                        bundle.getString("actionsMenu"),
                        KeyEvent.VK_A,
                        bundle.getString("actionsMenuDescription")
                ),
                generateMenuItem(
                        bundle.getString("quitItem"),
                        KeyEvent.VK_Q,
                        (event) -> exitConfirmation()
                )
        );
    }

    private void addMenu(JMenuBar menuBar, JMenu menu, JMenuItem... items) {
        for (JMenuItem item : items) {
            menu.add(item);
        }
        menuBar.add(menu);
    }

    private JMenu generateMenu(String menuName, int menuHotkey, String menuDescription) {
        JMenu jMenu = new JMenu(menuName);
        jMenu.setMnemonic(menuHotkey);
        jMenu.getAccessibleContext().setAccessibleDescription(menuDescription);
        return jMenu;
    }

    private JMenuItem generateMenuItem(String menuItemName, int menuItemMnemonic, ActionListener actionListener) {
        JMenuItem jMenuItem = new JMenuItem(menuItemName);
        jMenuItem.setMnemonic(menuItemMnemonic);
        jMenuItem.addActionListener(actionListener);
        return jMenuItem;
    }

    private void exitConfirmation() {
        Object[] choices = {bundle.getString("quit"), bundle.getString("cancel")};
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
            System.exit(0);
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
