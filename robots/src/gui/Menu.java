package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

public class Menu {

    private JMenu menu;

    public Menu(String s, Integer keyEvent, String description){
        menu = new JMenu(s);
        menu.setMnemonic(keyEvent);
        menu.getAccessibleContext().setAccessibleDescription(description);
    }

    public void createMenuItem(String text, Integer keyEvent, ActionListener actionListener){
        JMenuItem menuItem = new JMenuItem(text, keyEvent);
        menuItem.addActionListener(actionListener);
        menu.add(menuItem);
    }

    public JMenu getMenu() {
        return menu;
    }
}
