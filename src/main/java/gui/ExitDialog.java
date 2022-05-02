package gui;

import localization.LocalizationManager;

import javax.swing.*;

public class ExitDialog {
    private final LocalizationManager languageManager;
    public String yes;
    public String no;
    public String ask;
    public String title;

    public ExitDialog(LocalizationManager _languageManager, String _ask) {
        this.languageManager = _languageManager;
        yes = this.languageManager.getString("option.yes");
        no = this.languageManager.getString("option.no");
        title = this.languageManager.getString("exitAppMenu.text");
        ask = _ask;

    }

    public int show() {
        Object[] options = { yes, no };

        return JOptionPane
                .showOptionDialog(null, ask,
                        title, JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options,
                        options[0]);
    }


}
