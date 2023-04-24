package gui;

import javax.swing.*;
import java.awt.*;

public class ConfirmWindow {
    public static int confirmExit(Component component){
        return JOptionPane.showOptionDialog(component, "Закрыть?", "Выход",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"Да", "Нет"}, "Да");
    }
}