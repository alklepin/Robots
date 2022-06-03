package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import gui.functional.Disposable;
import listeners.WindowListenerImpl;
import localization.LocalizationManager;
import log.Logger;
import serialization.StorageHelper;
import serialization.WindowStorage;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame implements Disposable {
    private static final Locale LOCALE_RU = new Locale("ru");

    private static final int INSET = 50;

    public JInternalFrame logWindow, gameWindow;
    public WindowStorage windowStorage;
    public JInternalFrame[] windows;
    public StorageHelper helper;

    public MainApplicationFrame(LocalizationManager localizationManager, WindowStorage windowStorage) {
        this(localizationManager);
        this.windowStorage = windowStorage;
        this.windows = new JInternalFrame[] {logWindow, gameWindow};
        this.helper = new StorageHelper(windowStorage);

        ExitDialog dialog = new ExitDialog(localizationManager, localizationManager.getString("restoreWindow.ask"));

        var option = dialog.show();

        if (option == 0) {
            if (windowStorage != null && windowStorage.isRestored()) {
                helper.restoreWindows(this, windows);
            } else {
                setExtendedState(MAXIMIZED_BOTH);
                pack();
            }
        }
    }

    public MainApplicationFrame(LocalizationManager localizationManager) {
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(INSET, INSET, screenSize.width - INSET * 2, screenSize.height - INSET * 2);

        setContentPane(new JDesktopPane());
        setJMenuBar(new MenuGenerator(this).generateMenuBar(localizationManager));

        logWindow = createLogWindow(localizationManager);
        addWindow(logWindow);
        setMinimumSize(logWindow.getSize());
        Logger.debug("Протокол работает");

        gameWindow = new GameWindow(localizationManager, windowStorage);
        gameWindow.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                onClose(localizationManager, MainApplicationFrame.this);
            }
        });
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListenerImpl(localizationManager, windowStorage, this, helper, windows));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose(localizationManager, MainApplicationFrame.this);
            }
        });
    }

    protected LogWindow createLogWindow(LocalizationManager localizationManager)
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), localizationManager, windowStorage);
        logWindow.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                onClose(localizationManager, MainApplicationFrame.this);
            }
        });
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame)
    {
        add(frame).setVisible(true);
    }

    public void onClose(LocalizationManager localizationManager, Disposable disposable) {
        var exitDialog = new ExitDialog(localizationManager, localizationManager.getString("exitApp.ask"));
        var option = exitDialog.show();

        if (option == 0) {
            disposable.onDispose();
        }
    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if (windowStorage != null) {
            helper.storeWindows(this, windows);
        }
    }


}