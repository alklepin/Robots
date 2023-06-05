package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final GameWindow gameWindow;
    private final LogWindow logWindow;
    private final Properties cfg = new Properties();
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.

        //
        File configFile = new File("robots/src/config.properties");
        try{
            FileInputStream configInp = new FileInputStream(configFile.getAbsolutePath());
            cfg.load(configInp);
            Logger.debug("Окна загружены");
        } catch (Exception e){
            e.printStackTrace();
        }

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);


        logWindow = createLogWindow();
        gameWindow = createGameWindow();
        addWindow(logWindow);
        addWindow(gameWindow);
        loadWindows();

        setJMenuBar(generateMenuBar());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                closeProgram();
            }
        });
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow createGameWindow(){
        GameWindow gameWindow = new GameWindow();
        gameWindow.setVisible(true);
        gameWindow.setLocation(20, 20);
        gameWindow.setSize(400,400);
        setMinimumSize(gameWindow.getSize());
        return gameWindow;
    }

    private void saveWindows() {
        if (Boolean.parseBoolean(cfg.getProperty("isGameWindowSerializable"))) {
            gameWindow.save(cfg.getProperty("gameWindowOutPath"));
        }
        if (Boolean.parseBoolean(cfg.getProperty("isLogWindowSerializable"))) {
            logWindow.save(cfg.getProperty("logWindowOutPath"));
        }
    }

    private void loadWindows() {
        if (Boolean.parseBoolean(cfg.getProperty("isGameWindowSerializable"))) {
            gameWindow.load(cfg.getProperty("gameWindowOutPath"));
        }
        if (Boolean.parseBoolean(cfg.getProperty("isLogWindowSerializable"))) {
            logWindow.load(cfg.getProperty("logWindowOutPath"));
        }
        this.invalidate();
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }


    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(generateMappingMenu());
        menuBar.add(generateTestMenu());
        menuBar.add(generateOptionMenu());
        return menuBar;
    }

    private JMenu generateMappingMenu() {
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext()
                .setAccessibleDescription("Управление режимом отображения приложения");
        lookAndFeelMenu.add(generateMenuItem("Системная схема", KeyEvent.VK_S, event -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            invalidate();
        }));
        lookAndFeelMenu.add(generateMenuItem("Универсальная схема", KeyEvent.VK_S, event -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            invalidate();
        }));
        return lookAndFeelMenu;
    }

    private JMenu generateTestMenu() {
        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");
        testMenu.add(generateMenuItem("Сообщение в лог", KeyEvent.VK_S, event -> {
            Logger.debug("Новая строка");
        }));
        return testMenu;
    }

    private JMenu generateOptionMenu() {
        JMenu optionMenu = new JMenu("Дополнительно");
        optionMenu.setMnemonic(KeyEvent.VK_ESCAPE);
        optionMenu.getAccessibleContext().setAccessibleDescription("Закрыть");
        optionMenu.add(generateMenuItem("Выход", KeyEvent.VK_ESCAPE, event -> {
            closeProgram();
        }));
        return optionMenu;
    }


    private JMenuItem generateMenuItem(String name, int keyEvent, ActionListener actionListener) {
        JMenuItem item = new JMenuItem(name, keyEvent);
        item.addActionListener(actionListener);
        return item;
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

    //Общая функция для выхода
    private void closeProgram() {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        int YesNoBox = JOptionPane.showConfirmDialog(this, "Вы уверены?",
                "Окно подтверждения",
                JOptionPane.YES_NO_OPTION);
        if (YesNoBox == 0) {
            saveWindows();
            System.exit(0);
        }
    }
}
