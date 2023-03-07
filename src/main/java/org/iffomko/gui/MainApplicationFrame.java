package org.iffomko.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;

import javax.swing.*;

import org.iffomko.log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane(); // окно с ещё окнами внутри

    /**
     * Конструктор, который создает контейнер с окнами: окно с игрой, окно с логами, генерирует меню. И настраивает их.
     */
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        JMenuBar menuBar = MenuBar.getMenu();

        menuBar.getMenu(0).getItem(0).addActionListener((event) -> {
            this.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });

        menuBar.getMenu(0).getItem(1).addActionListener((event) -> {
            this.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });

        menuBar.getMenu(1).getItem(0).addActionListener((event) -> Logger.debug("Новая строка"));

        menuBar.getMenu(2).getItem(0).addActionListener((event) -> {
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
                    new WindowEvent(this, WindowEvent.WINDOW_CLOSING)
            );
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
    public void setLookAndFeel(String className)
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
