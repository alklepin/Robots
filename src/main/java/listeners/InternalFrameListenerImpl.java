package listeners;

import gui.ExitDialog;
import localization.LocalizationManager;
import serialization.WindowStorage;

import javax.swing.*;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class InternalFrameListenerImpl implements InternalFrameListener {

    private final LocalizationManager languageManager;

    public InternalFrameListenerImpl(LocalizationManager _languageManager) {
        this.languageManager = _languageManager;
    }

    public void internalFrameClosing(InternalFrameEvent e) {
        var dialog = new ExitDialog(this.languageManager, this.languageManager.getString("closeWindow.ask"));
        var option = dialog.show();

        if (option == 0) {
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
