package org.iffomko.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import org.iffomko.log.Logger;
import org.iffomko.savers.Savable;
import org.iffomko.savers.SaverException;
import org.iffomko.savers.StateKeeper;

/**
 * Приложение со всеми окнами
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane(); // окно с ещё окнами внутри
    private final static ResourceBundle resourceBundle = ResourceBundle.getBundle(
            "org.iffomko.gui.localizationProperties.mainApplicationFrame.MainApplicationFrameResource",
            new Locale(System.getProperty("user.language"), System.getProperty("user.country"))
    );

    /**
     * <p>Обработчик события при нажатии на кнопку, которая позволяет выбрать .jar файл с настройками робота</p>
     */
    private static void onLoadRobot() {
        UIManager.put("FileChooser.cancelButtonText", resourceBundle.getString("FileChooser.cancelButtonText"));
        UIManager.put("FileChooser.fileNameLabelText", resourceBundle.getString("FileChooser.fileNameLabelText"));
        UIManager.put("FileChooser.filesOfTypeLabelText", resourceBundle.getString("FileChooser.filesOfTypeLabelText"));
        UIManager.put("FileChooser.lookInLabelText", resourceBundle.getString("FileChooser.lookInLabelText"));
        UIManager.put("FileChooser.folderNameLabelText", resourceBundle.getString("FileChooser.folderNameLabelText"));

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(resourceBundle.getString("selectRobotJarTitle"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }

                return f.getName().endsWith(".jar");
            }

            @Override
            public String getDescription() {
                return resourceBundle.getString("selectRobotJarDescription");
            }
        });

        int result = fileChooser.showDialog(this.get, resourceBundle.getString("selectRobotJarApproveBtn"));

        if (
                result == JFileChooser.APPROVE_OPTION &&
                        fileChooser.getSelectedFile().getPath().toLowerCase().endsWith(".jar")
        ) {
            String robotPath = fileChooser.getSelectedFile().getPath();
            System.out.println("Все супер! Мы выбрали робота!\n" + robotPath);

            // ToDo: тут надо запустить загрузчик робота
        }
    }

    /**
     * Конструктор, который создает контейнер с окнами: окно с игрой, окно с логами, генерирует меню. И настраивает их.
     */
    public MainApplicationFrame() {
        StateKeeper stateKeeper = StateKeeper.getInstance();

        stateKeeper.restore();

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        LogWindow logWindow = createLogWindow();

        try {
            logWindow.restore(stateKeeper.getState(logWindow.getPrefix()));
        } catch (SaverException e) {
            e.printStackTrace();
        }

        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();

        try {
            gameWindow.restore(stateKeeper.getState(gameWindow.getPrefix()));
        } catch (SaverException e) {
            e.printStackTrace();
        }

        addWindow(gameWindow);

        JMenuBar menuBar = MenuBar.getMenu(this);
        menuBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Получил событие");
            }
        });

        setJMenuBar(menuBar);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing(e);
            }
        });
    }

    /**
     * Создает окно с логами и настраивает его
     * @return - возвращает объект окна с логами
     */
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    /**
     * Добавляет фрейм в контейнер с окнами
     * @param frame - фрейм, который нужно добавить
     */
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
    /**
     * Метод, который дергается, когда приложение хочет закрыться
     * @param event - информация о событии
     */
    private void onWindowClosing(WindowEvent event) {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        UIManager.put("OptionPane.yesButtonText", resourceBundle.getString("yesButtonText"));
        UIManager.put("OptionPane.noButtonText", resourceBundle.getString("noButtonText"));

        int response = JOptionPane.showOptionDialog(
                event.getWindow(),
                resourceBundle.getString("closeApp"),
                resourceBundle.getString("confirmation"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );

        if (response == JOptionPane.YES_OPTION) {
            StateKeeper stateKeeper = StateKeeper.getInstance();

            try {
                for (JInternalFrame frame : desktopPane.getAllFrames()) {
                    if (frame instanceof Savable) {
                        stateKeeper.addState((Savable) frame);
                    }
                }
            } catch (SaverException e) {
                e.printStackTrace();
            }

            stateKeeper.save();

            event.getWindow().setVisible(false);
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(event.getWindow(), WindowEvent.WINDOW_CLOSING)
            );
            setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
    }

    /**
     * Устанавливает текущий стиль окошка и обновляет внешний вид от рисованного UI
     * @param className - имя класса, стиль которого нужно установить
     */
    void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
