package gui;

import javax.swing.*;

enum DisplayMode {
    METAL("javax.swing.plaf.metal.MetalLookAndFeel", "Metal"),
    NIMBUS("javax.swing.plaf.nimbus.NimbusLookAndFeel", "Nimbus"),
    CROSS_PLATFORM(UIManager.getSystemLookAndFeelClassName(), "Универсальная схема"),
    SYSTEM(UIManager.getCrossPlatformLookAndFeelClassName(), "Системная схема");

    public final String className;
    public final String description;

    DisplayMode(String className, String description) {
        this.className = className;
        this.description = description;
    }
}
