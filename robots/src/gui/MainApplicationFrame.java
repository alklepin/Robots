package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import log.Logger;

public class MainApplicationFrame extends JFrame {
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
        Logger.debug("Протокол работает");
        return logWindow;
    }

    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu: Документ
        JMenu documentMenu = new JMenu("Меню");
        documentMenu.setMnemonic(KeyEvent.VK_D);

        JMenuItem newGameItem = new JMenuItem("Новое игровое поле", KeyEvent.VK_N);
        newGameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
        newGameItem.addActionListener((event) -> addWindow(new GameWindow()));
        documentMenu.add(newGameItem);

        JMenuItem logItem = new JMenuItem("Окно логов", KeyEvent.VK_L);
        logItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        logItem.addActionListener((event) -> addWindow(new LogWindow(Logger.getDefaultLogSource())));
        documentMenu.add(logItem);

        JMenuItem exitItem = new JMenuItem("Выход", KeyEvent.VK_Q);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        exitItem.addActionListener((event) -> confirmExit());
        documentMenu.add(exitItem);

        menuBar.add(documentMenu);

        // Menu: Режим отображения
        JMenu viewMenu = new JMenu("Режим отображения");
        viewMenu.setMnemonic(KeyEvent.VK_V);

        viewMenu.add(createLookAndFeelMenuItem("Системная схема", UIManager.getSystemLookAndFeelClassName()));
        viewMenu.add(createLookAndFeelMenuItem("Универсальная схема", UIManager.getCrossPlatformLookAndFeelClassName()));

        menuBar.add(viewMenu);

        // Menu: Тесты
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);

        testMenu.add(createTestMenuItem("Сообщение в лог", () -> Logger.debug("Новая строка")));

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
            // handle exception properly
            e.printStackTrace();
        }
    }

    private void confirmExit() {
        String message = "Вы уверены,что хотите выйти?";
        int confirmation = JOptionPane.showConfirmDialog(this, message, "Подтверждение выхода", JOptionPane.YES_NO_OPTION);
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