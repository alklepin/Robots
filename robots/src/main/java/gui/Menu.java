package gui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Menu {

    private JMenu newMenu;

    public Menu(String s, String accessDescription, Integer menuKeyEvent) {
        newMenu = new JMenu(s);
        newMenu.setMnemonic(menuKeyEvent);
        newMenu.getAccessibleContext().setAccessibleDescription(accessDescription);
    }

    public void createMenu(String menuItemText, ActionListener actionListener, Integer itemKeyEvent) {
        JMenuItem menuItem = new JMenuItem(menuItemText, itemKeyEvent);
        menuItem.addActionListener(actionListener);
        newMenu.add(menuItem);
    }
    public JMenu getMenu() {
        return newMenu;
    }
}
