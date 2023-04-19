package gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import core.Texts;
import log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        addWindow(GUIManager.create(createLogWindow()));

        addWindow(GUIManager.create(createGameWindow()));

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitEvent();
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(Texts.get("log.testMessage"));
        return logWindow;
    }

    private GameWindow createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        setMinimumSize(gameWindow.getSize());

        return gameWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }

    private void exitEvent() {
        Object[] options = {Texts.get("yes"), Texts.get("no")};
        int answer = JOptionPane.showOptionDialog(
                this, Texts.get("exit.text"), Texts.get("exit.title"),
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

        if (answer == 0) {
            GUIManager.removeAll();

            Frame[] frames = Frame.getFrames();
            for (Frame frame : frames) {
                frame.setVisible(false);
                frame.dispose();
            }

            System.exit(0);
        }
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createFileMenu());
        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createLanguageMenu());

        return menuBar;
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

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu(Texts.get("menuBar.file"));
        fileMenu.add(createItem(Texts.get("menuBar.file.exit"), 0, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitEvent();
            }
        }));
        return fileMenu;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu(Texts.get("menuBar.displayMode"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        lookAndFeelMenu.add(createItem(Texts.get("menuBar.displayMode.systemMode"), KeyEvent.VK_S, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                MainApplicationFrame.this.invalidate();
            }
        }));

        lookAndFeelMenu.add(createItem(Texts.get("menuBar.displayMode.universalMode"), KeyEvent.VK_S, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                MainApplicationFrame.this.invalidate();
            }
        }));

        return lookAndFeelMenu;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu(Texts.get("menuBar.tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        testMenu.add(createItem(Texts.get("menuBar.tests.logMessage"), KeyEvent.VK_S, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Logger.debug("Новая строка");
            }
        }));

        return testMenu;
    }

    private JMenu createLanguageMenu() {
        JMenu languageMenu = new JMenu(Texts.get("menuBar.language"));

        languageMenu.add(createItem(Texts.get("menuBar.language.ru"), 0, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Texts.setLanguage("lang_ru");
                MainApplicationFrame.this.getContentPane().validate();
            }
        }));
        languageMenu.add(createItem(Texts.get("menuBar.language.en"), 0, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Texts.setLanguage("lang_en");
                MainApplicationFrame.this.revalidate();
                MainApplicationFrame.this.repaint();
            }
        }));

        return languageMenu;
    }

    private JMenuItem createItem(String name, int mnemonic, Action action) {
        JMenuItem item = new JMenuItem();
        item.setAction(action);
        item.setText(name);
        item.setMnemonic(mnemonic);

        return item;
    }
}
