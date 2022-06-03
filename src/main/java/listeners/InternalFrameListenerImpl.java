package listeners;

import gui.ExitDialog;
import localization.LocalizationManager;
import serialization.StorageHelper;
import serialization.WindowStorage;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class InternalFrameListenerImpl implements InternalFrameListener {

    private final LocalizationManager languageManager;
    private WindowStorage windowStorage;
    private JInternalFrame frame;


    public InternalFrameListenerImpl(LocalizationManager _languageManager, WindowStorage _windowStorage, JInternalFrame _frame) {
        this.languageManager = _languageManager;
        this.windowStorage = _windowStorage;
        this.frame = _frame;
    }

    public void internalFrameClosing(InternalFrameEvent e) {
        var dialog = new ExitDialog(this.languageManager, this.languageManager.getString("closeWindow.ask"));
        var option = dialog.show();

        if (option == 0) {
            if (windowStorage != null) {
                this.windowStorage.store(this.frame.getClass().toString(), this.frame);
            }
            e.getInternalFrame().dispose();
        }
    }

    public void internalFrameClosed(InternalFrameEvent e) {

    }
    public void internalFrameOpened(InternalFrameEvent e) {

    }
    public void internalFrameIconified(InternalFrameEvent e) {

    }
    public void internalFrameDeiconified(InternalFrameEvent e) {

    }
    public void internalFrameActivated(InternalFrameEvent e) {

    }
    public void internalFrameDeactivated(InternalFrameEvent e) {

    }
}