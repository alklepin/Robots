package listeners;

import gui.ExitDialog;
import gui.MainApplicationFrame;
import localization.LocalizationManager;
import serialization.StorageHelper;
import serialization.WindowStorage;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class WindowListenerImpl implements WindowListener {

    private final LocalizationManager languageManager;
    private WindowStorage windowStorage;
    private JFrame frame;
    private StorageHelper helper;
    private JInternalFrame[] windows;

    public WindowListenerImpl(LocalizationManager _languageManager, WindowStorage _windowStorage, JFrame _frame, StorageHelper _helper, JInternalFrame[] _windows) {
        this.languageManager = _languageManager;
        this.windowStorage = _windowStorage;
        this.frame = _frame;
        this.helper = _helper;
        this.windows = _windows;
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent event) {
        var dialog = new ExitDialog(this.languageManager, this.languageManager.getString("exitApp.ask"));
        var option = dialog.show();

        if (option == 0) {
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            if (windowStorage != null) {
                helper.storeWindows(frame, windows);
            }
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}