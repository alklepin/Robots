package listeners;

import gui.ExitDialog;
import localization.LocalizationManager;

import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class MenuListenerImpl implements MenuListener {
    private final LocalizationManager languageManager;

    public MenuListenerImpl(LocalizationManager _languageManager) {
        this.languageManager = _languageManager;
    }

    public void menuSelected(MenuEvent e) {
        var dialog = new ExitDialog(this.languageManager, this.languageManager.getString("exitApp.ask"));
        var option = dialog.show();

        if (option == 0) {
            System.exit(0);
        }
    }
    public void menuDeselected(MenuEvent e) {
    }
    public void menuCanceled(MenuEvent e) {
    }
}
