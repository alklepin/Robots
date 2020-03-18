package gui;

import log.LogChangeListener;
import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MenuBar extends JMenuBar {
    private JFrame parentObj;

    public MenuBar(JFrame par)
    {
        parentObj = par;

        add(createMenuCategory_LookAndFeel());
        add(createMenuCategory_Test());
        add(createMenuCategory_Tools());
    }

    private JMenu createMenuCategory(String name, int key, String descriprion)
    {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(key);
        menu.getAccessibleContext().setAccessibleDescription(descriprion);
        return menu;
    }

    private JMenuItem createMenuItem(String name, int key, ActionListener listener)
    {
        JMenuItem menuItem = new JMenuItem(name, key);
        menuItem.addActionListener(listener);
        return menuItem;
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(parentObj);
        }
        catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }

    private JMenu createMenuCategory_LookAndFeel()
    {
        JMenu lookAndFeelMenu = createMenuCategory("Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения");

        JMenuItem systemLook = createMenuItem("Системная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(systemLook);

        JMenuItem universalLook = createMenuItem("Универсальная схема", KeyEvent.VK_S,(event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        lookAndFeelMenu.add(universalLook);

        return lookAndFeelMenu;
    }

    private JMenu createMenuCategory_Test()
    {
        JMenu testMenu = createMenuCategory("Тесты", KeyEvent.VK_T, "Тестовые команды");

        JMenuItem logMessage = createMenuItem("Сообщение в лог", KeyEvent.VK_S, (event) -> {
            Logger.debug("Новая строка");
        });
        testMenu.add(logMessage);

        return testMenu;
    }

    private JMenu createMenuCategory_Tools()
    {
        JMenu toolMenu = createMenuCategory("Инструменты", KeyEvent.VK_I, "Инструменты");

        JMenuItem exitItem = createMenuItem("Выход", KeyEvent.VK_E, (event) -> {
            if (JOptionPane.showConfirmDialog(this.getParent(),
                    "Are you sure you want to close this window?", "Close Window?",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        });

        toolMenu.add(exitItem);

        return toolMenu;
    }
}
