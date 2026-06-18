package gui;

import localization.LocalizationManager;
import log.Logger;

import javax.swing.*;
import java.util.Locale;

public class MenuBarFactory {
    private final MainApplicationFrame frame;
    private final LocalizationManager loc = LocalizationManager.getInstance();

    public MenuBarFactory(MainApplicationFrame frame) {
        this.frame = frame;
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createWindowsMenu());
        menuBar.add(createLanguageMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu(loc.getString("menu.file"));
        JMenuItem exitItem = new JMenuItem(loc.getString("menu.exit"));
        exitItem.addActionListener(e -> frame.exitApplication());
        fileMenu.add(exitItem);
        return fileMenu;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu(loc.getString("menu.lookandfeel"));
        JMenuItem system = new JMenuItem(loc.getString("menu.lookandfeel.system"));
        system.addActionListener(e -> {
            frame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            frame.invalidate();
        });
        JMenuItem cross = new JMenuItem(loc.getString("menu.lookandfeel.cross"));
        cross.addActionListener(e -> {
            frame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            frame.invalidate();
        });
        menu.add(system);
        menu.add(cross);
        return menu;
    }

    private JMenu createTestMenu() {
        JMenu menu = new JMenu(loc.getString("menu.test"));
        JMenuItem logMsg = new JMenuItem(loc.getString("menu.test.log"));
        logMsg.addActionListener(e -> Logger.debug(loc.getString("log.newline")));
        menu.add(logMsg);
        return menu;
    }

    private JMenu createWindowsMenu() {
        JMenu menu = new JMenu(loc.getString("menu.windows"));
        JMenuItem showLog = new JMenuItem(loc.getString("menu.windows.showLog"));
        showLog.addActionListener(e -> frame.showWindowByName("LogWindow"));
        JMenuItem showGame = new JMenuItem(loc.getString("menu.windows.showGame"));
        showGame.addActionListener(e -> frame.showWindowByName("GameWindow"));
        JMenuItem showCoord = new JMenuItem(loc.getString("menu.windows.showCoord"));
        showCoord.addActionListener(e -> frame.showWindowByName("CoordWindow"));
        menu.add(showLog);
        menu.add(showGame);
        menu.add(showCoord);
        return menu;
    }

    private JMenu createLanguageMenu() {
        JMenu menu = new JMenu(loc.getString("menu.language"));
        JMenuItem russian = new JMenuItem(loc.getString("menu.language.russian"));
        russian.addActionListener(e -> LocalizationManager.getInstance().setLocale(new Locale("ru")));
        JMenuItem english = new JMenuItem(loc.getString("menu.language.english"));
        english.addActionListener(e -> LocalizationManager.getInstance().setLocale(new Locale("en")));
        menu.add(russian);
        menu.add(english);
        return menu;
    }
}
