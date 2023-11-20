package main.java.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import main.java.log.Logger;
import main.java.view.ViewModel;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame(ViewModel gameViewModel) {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);
        this.setContentPane(this.desktopPane);
        LogWindow logWindow = this.createLogWindow();
        this.addWindow(logWindow);
        GameWindow gameWindow = gameViewModel.getGameWindow();
        this.addWindow(gameWindow);
        this.setJMenuBar(this.generateMenuBar());
        this.setDefaultCloseOperation(3);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(610, 0);
        logWindow.setSize(300, 800);
        this.setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        this.desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenu createMenu(String nameOfMenu, String description, int mnemonic) {
        JMenu lookAndFeelMenu = new JMenu(nameOfMenu);
        lookAndFeelMenu.setMnemonic(86);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(description);
        return lookAndFeelMenu;
    }

    private JMenuItem createMenuItem(String name, ActionListener l) {
        JMenuItem systemLookAndFeel = new JMenuItem(name, 83);
        systemLookAndFeel.addActionListener(l);
        return systemLookAndFeel;
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu lookAndFeelMenu = this.createMenu("Режим отображения", "Управление режимом отображения приложения", 86);
        JMenuItem systemLookAndFeel = this.createMenuItem("Системная схема", (event) -> {
            this.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLookAndFeel);
        JMenuItem crossplatformLookAndFeel = this.createMenuItem("Универсальная схема", (event) -> {
            this.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(crossplatformLookAndFeel);
        JMenu testMenu = this.createMenu("Тесты", "Тестовые команды", 83);
        JMenuItem addLogMessageItem = this.createMenuItem("Сообщение в лог", (event) -> {
            Logger.debug("Новая строка");
        });
        testMenu.add(addLogMessageItem);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException | ClassNotFoundException var3) {
        }

    }
}
