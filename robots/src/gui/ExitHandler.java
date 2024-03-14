package gui;

import javax.swing.*;

public class ExitHandler {

    public void handle() {
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        int result = JOptionPane.showConfirmDialog(
                null,
                "Вы уверенны что хотите выйти?",
                "Окно подтверждения",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
