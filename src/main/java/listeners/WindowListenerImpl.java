package listeners;

import gui.ExitDialog;
import localization.LocalizationManager;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class WindowListenerImpl implements WindowListener {

    private final LocalizationManager languageManager;

    public WindowListenerImpl(LocalizationManager _languageManager) {
        this.languageManager = _languageManager;
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent event) {
        var dialog = new ExitDialog(this.languageManager, this.languageManager.getString("exitApp.ask"));
        var option = dialog.show();

        if (option == 0) {
            System.exit(0);
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
