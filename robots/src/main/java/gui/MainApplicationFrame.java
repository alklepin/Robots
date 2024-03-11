package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import log.Logger;

import java.util.Locale;
import java.util.ResourceBundle;

public class MainApplicationFrame extends JFrame {
    Locale locale = new Locale("ru", "RU");
    ResourceBundle bundle = ResourceBundle.getBundle("recources", locale);
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        initializeUI();
    }

    private void initializeUI() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        setContentPane(desktopPane);

        addWindow(createLogWindow());
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(bundle.getString("protocol"));
        return logWindow;
    }

    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();


        JMenu documentMenu = new JMenu(bundle.getString("menu"));
        documentMenu.setMnemonic(KeyEvent.VK_D);

        JMenuItem newGameItem = new JMenuItem(bundle.getString("new_game_field"), KeyEvent.VK_N);
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        newGameItem.addActionListener((event) -> addWindow(new GameWindow()));
        documentMenu.add(newGameItem);

        JMenuItem logItem = new JMenuItem(bundle.getString("log_window"), KeyEvent.VK_L);
        logItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        logItem.addActionListener((event) -> addWindow(new LogWindow(Logger.getDefaultLogSource())));
        documentMenu.add(logItem);

        JMenuItem exitItem = new JMenuItem(bundle.getString("exit"), KeyEvent.VK_Q);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        exitItem.addActionListener((event) -> confirmExit());
        documentMenu.add(exitItem);

        menuBar.add(documentMenu);

        JMenu viewMenu = new JMenu(bundle.getString("display_mode"));
        viewMenu.setMnemonic(KeyEvent.VK_V);

        viewMenu.add(createLookAndFeelMenuItem(bundle.getString("system_diagram"), UIManager.getSystemLookAndFeelClassName()));
        viewMenu.add(createLookAndFeelMenuItem(bundle.getString("universal_scheme"), UIManager.getCrossPlatformLookAndFeelClassName()));

        menuBar.add(viewMenu);

        JMenu testMenu = new JMenu(bundle.getString("tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);

        testMenu.add(createTestMenuItem(bundle.getString("message_in_the_log"), () -> Logger.debug(bundle.getString("new_str"))));

        menuBar.add(testMenu);

        return menuBar;
    }

    private JMenuItem createLookAndFeelMenuItem(String label, String className) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener((event) -> setLookAndFeel(className));
        return item;
    }

    private JMenuItem createTestMenuItem(String label, Runnable action) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener((event) -> action.run());
        return item;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirmExit() {
        String message = bundle.getString("exit?");
        UIManager.put("OptionPane.yesButtonText", bundle.getString("yes_button_text"));
        UIManager.put("OptionPane.noButtonText", bundle.getString("no_button_text"));
        int confirmation = JOptionPane.showConfirmDialog(this, message, bundle.getString("exit-yes"), JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.setVisible(true);
        });
    }
}