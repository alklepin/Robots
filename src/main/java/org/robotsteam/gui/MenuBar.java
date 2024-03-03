package org.robotsteam.gui;

import org.robotsteam.log.Logger;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;

public class MenuBar extends JMenuBar {
    private final JFrame parent;

    public MenuBar(JFrame parent) {
        this.parent = parent;

        this.add(initTestMenu());
        this.add(initLookAndFeelMenu());
    }

    private JMenu initTestMenu() {
        JMenu menu = createJMenu(
                "Тесты", KeyEvent.VK_T,
                "Тестовые команды"
        );

        menu.add(createJMenuItem(
                "Сообщение в лог", KeyEvent.VK_S,
                e -> Logger.debug("Новая строка")
        ));

        return menu;
    }

    private JMenu initLookAndFeelMenu() {
        JMenu menu = createJMenu(
                "Режим отображения", KeyEvent.VK_V,
                "Управление режимом отображения приложения"
        );

        menu.add(createJMenuItem(
                "Системная схема", KeyEvent.VK_S,
                e -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        ));

        menu.add(createJMenuItem(
                "Универсальная схема", KeyEvent.VK_S,
                e -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())
        ));

        return menu;
    }

    private JMenu createJMenu(String label, int mnemonic, String accessDesc) {
        JMenu menu = new JMenu(label);
        menu.setMnemonic(mnemonic);
        menu.getAccessibleContext().setAccessibleDescription(accessDesc);

        return menu;
    }

    private JMenuItem createJMenuItem(String label, int mnemonic, ActionListener callback) {
        JMenuItem menuItem = new JMenuItem(label, mnemonic);
        menuItem.addActionListener(callback);

        return menuItem;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(parent);
        }
        catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
        finally { parent.invalidate(); }
    }
}
