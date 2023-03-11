package org.iffomko.gui.mainApplicationFrame;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;

import javax.swing.*;

import org.iffomko.gui.GameWindow;
import org.iffomko.gui.LogWindow;
import org.iffomko.log.Logger;
import org.iffomko.savers.ApplicationSaver;
import org.iffomko.savers.ComponentSaver;

/**
 * Приложение со всеми окнами
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane(); // окно с ещё окнами внутри

    /**
     * Конструктор, который создает контейнер с окнами: окно с игрой, окно с логами, генерирует меню. И настраивает их.
     */
    public MainApplicationFrame() {
        ApplicationSaver.getInstance().restore();

        String gameWindowName = "game";
        String logWindowName = "log";

        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        LogWindow logWindow = createLogWindow();
        logWindow.setName(logWindowName);

        restoreFrame(logWindow, logWindowName);

        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        gameWindow.setName(gameWindowName);

        restoreFrame(gameWindow, gameWindowName);

        addWindow(gameWindow);

        setJMenuBar(MenuBar.getMenu(this));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing(e);
            }
        });
    }

    /**
     * <p>Восстанавливает настройки фрейма: такие как свернутость, расположение на экране и т. д.</p>
     * @param frame - фрейм, у которого нужно сохранить настройки
     * @param name - имя фрейма
     */
    private void restoreFrame(JInternalFrame frame, String name) {
        ApplicationSaver applicationSaver = ApplicationSaver.getInstance();

        ComponentSaver gameSave = applicationSaver.getState(name);

        if (!applicationSaver.isRestored()) {
            return;
        }

        Point gameLocation = new Point();
        gameLocation.setLocation(Double.parseDouble(gameSave.get("x")), Double.parseDouble(gameSave.get("y")));

        frame.setLocation(gameLocation);

        try {
            frame.setIcon(Boolean.parseBoolean(gameSave.get("state")));
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }

        Dimension frameSize = new Dimension(
                Integer.parseInt(gameSave.get("width")),
                Integer.parseInt(gameSave.get("height"))
        );

        frame.setSize(frameSize);
    }

    /**
     * <p>Сохраняет все фреймы внутри desktopPane</p>
     * <p>Если у фрейма нет имени, то вылетит ошибка <code>SaversException</code></p>
     */
    private void saveFrames() {
        ApplicationSaver applicationSaver = ApplicationSaver.getInstance();

        for (JInternalFrame frame : desktopPane.getAllFrames()) {
            ComponentSaver componentSaver = new ComponentSaver(frame.getName());

            Point locationPoint = frame.getLocation();

            componentSaver.put("x", String.valueOf(locationPoint.getX()));
            componentSaver.put("y", String.valueOf(locationPoint.getY()));
            componentSaver.put("state", String.valueOf(frame.isIcon()));
            componentSaver.put("width", String.valueOf(frame.getWidth()));
            componentSaver.put("height", String.valueOf(frame.getHeight()));

            applicationSaver.addState(componentSaver);
        }

        applicationSaver.save();
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

        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");

        int response = JOptionPane.showOptionDialog(
                event.getWindow(),
                "Закрыть приложение?",
                "Подтверждение",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );

        if (response == JOptionPane.YES_OPTION) {
            saveFrames();

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
