package robots.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Set;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import robots.data.DataContainer;
import robots.data.ReaderFromResouce;
import robots.log.Logger;

/**
 * Что требуется сделать: 1. Метод создания меню перегружен функционалом и трудно читается. Следует
 * разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    static private final DataContainer DC = DataContainer.getInstance();
    private final String[] options =
            {DC.getContentNoException("yes"), DC.getContentNoException("no")};

    public MainApplicationFrame() {
        // Make the big window be indented 50 pixels from each edge
        // of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);


        addWindow(createLogWindow());

        addWindow(new GameWindow(), 400, 400);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setExitAction();
    }

    private void setExitAction() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitConfirm();
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(DC.getContentNoException("logger/protocol_working")); // "Протокол работает"
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    protected void addWindow(JInternalFrame frame, int h, int w) {
        frame.setSize(w, h);
        addWindow(frame);
    }

    // protected JMenuBar createMenuBar() {
    // JMenuBar menuBar = new JMenuBar();
    //
    // //Set up the lone menu.
    // JMenu menu = new JMenu("Document");
    // menu.setMnemonic(KeyEvent.VK_D);
    // menuBar.add(menu);
    //
    // //Set up the first menu item.
    // JMenuItem menuItem = new JMenuItem("New");
    // menuItem.setMnemonic(KeyEvent.VK_N);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(
    // KeyEvent.VK_N, ActionEvent.ALT_MASK));
    // menuItem.setActionCommand("new");
    //// menuItem.addActionListener(this);
    // menu.add(menuItem);
    //
    // //Set up the second menu item.
    // menuItem = new JMenuItem("Quit");
    // menuItem.setMnemonic(KeyEvent.VK_Q);
    // menuItem.setAccelerator(KeyStroke.getKeyStroke(
    // KeyEvent.VK_Q, ActionEvent.ALT_MASK));
    // menuItem.setActionCommand("quit");
    //// menuItem.addActionListener(this);
    // menu.add(menuItem);
    //
    // return menuBar;
    // }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // JMenu testMenu = new JMenu(DC.getContentNoException("menu/test/title")); // "Тесты"
        // addLogMessageTo(testMenu);

        // JMenu actionsMenu = new JMenu(DC.getContentNoException("menu/action/title")); // "Действия"
        // addCloseOption(actionsMenu);

        JMenu langMenu = new JMenu(DC.getContentNoException("menu/lang/title"));
        addLanguages(langMenu);

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(langMenu);
        menuBar.add(createActionsMenu());
        return menuBar;
    }

    private JMenu createActionsMenu() {
        JMenu actionsMenu = new JMenu(DC.getContentNoException("menu/action/title")); // "Действия"
        // addCloseOption(actionsMenu);
        actionsMenu.add(createProperties( //
                DC.getContentNoException("menu/action/exit/name"), // "Выйти"
                KeyEvent.VK_Q, //
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.SHIFT_DOWN_MASK), //
                (event) -> {
                    WindowEvent closeEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
                }));
        return actionsMenu;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu(DC.getContentNoException("menu/test/title")); // "Тесты"
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription( //
                DC.getContentNoException("menu/test/description"));
        testMenu.add(createProperties( //
                DC.getContentNoException("menu/test/items/massage_to_log"), //
                KeyEvent.VK_S, null, //
                (event) -> {
                    Logger.debug(DC.getContentNoException("logger/debug_test")); // "Новая строка"
                }));
        return testMenu;
    }

    private JMenu createLookAndFeelMenu() {
        JMenu lookAndFeelMenu = new JMenu( //
                DC.getContentNoException("menu/view/title")); // "Режим отображения"
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext()
                .setAccessibleDescription(DC.getContentNoException("menu/view/description"));
        lookAndFeelMenu
                .add(createProperties(DC.getContentNoException("menu/view/items/system_shame"), // ,
                                                                                                // //
                        KeyEvent.VK_S, null, (event) -> {
                            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                            this.invalidate();
                        }));

        lookAndFeelMenu
                .add(createProperties(DC.getContentNoException("menu/view/items/universal_shame"), //
                        KeyEvent.VK_S, null, (event) -> {
                            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                            this.invalidate();
                        }));
        return lookAndFeelMenu;
    }

    private JMenuItem createProperties(String bottonName, int bottonMnemonic, KeyStroke accelerator,
            ActionListener listener) {
        JMenuItem button = new JMenuItem(bottonName, bottonMnemonic);
        button.addActionListener(listener);
        if (null != accelerator) {
            button.setAccelerator(accelerator);
        }
        return button;
    }

    private void addLanguages(JMenu menu) {
        try {
            String path = "localization/";
            Set<String> locales = ReaderFromResouce.getAllFiles(path);
            final String sSure = DC.getContentNoException("menu/lang/change_lang/sure");
            final String sChange = DC.getContentNoException("menu/lang/change_lang/change");
            for (String locale : locales) {
                String clearLocale =
                        locale.substring(path.length()).replaceAll("(?<=\\w+)\\..+$", "");
                JMenuItem button = new JMenuItem(clearLocale);
                button.addActionListener((event) -> {
                    int sure = JOptionPane.showOptionDialog(this, sSure, sChange + clearLocale,
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
                            options[1]);
                    if (JOptionPane.YES_NO_OPTION == sure) {
                        DC.rememberLocale(clearLocale);
                    }
                });
                menu.add(button);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void exitConfirm() {
        int sure = JOptionPane.showOptionDialog(this,
                DC.getContentNoException("menu/action/exit/sure"), // "Вы уверены?"
                DC.getContentNoException("menu/action/exit/close"), // "Закрыть приложение? "
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if (JOptionPane.YES_OPTION == sure) {
            try {
                DC.saveTheRememberedLocale();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            WindowEvent closeEvent = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
        }
    }


    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
