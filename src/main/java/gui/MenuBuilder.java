package gui;

import localization.LocalizationManager;

import javax.swing.*;
import java.awt.event.ActionListener;

class MenuBuilder {

    private final LocalizationManager languageManager;
    private final JMenu menu;

    MenuBuilder(LocalizationManager languageManager) {
        this.languageManager = languageManager;
        menu = new JMenu();
    }

    MenuBuilder setText(String key) {
        languageManager.bindField(key, menu::setText);
        return this;
    }

    MenuBuilder setMnemonic(int mnemonic) {
        menu.setMnemonic(mnemonic);
        return this;
    }

    MenuBuilder setDescription(String key) {
        languageManager.bindField(key, menu.getAccessibleContext()::setAccessibleDescription);
        return this;
    }

    MenuBuilder addMenuItem(String textKey, int mnemonic, ActionListener listener) {
        var item = new JMenuItem();
        item.setMnemonic(mnemonic);
        item.addActionListener(listener);
        languageManager.bindField(textKey, item::setText);
        menu.add(item);
        return this;
    }

    JMenu build() {
        return menu;
    }
}
