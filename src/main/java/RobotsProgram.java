import gui.MainApplicationFrame;
import localization.LocalizationManager;
import serialization.WindowStorage;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {

    private static final String RESOURCES_NAME = "Resources";
    private static final String WINDOW_PATH = "window.ser";

    public static void main(String[] args) {
     try {
       UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
     } catch (Exception e) {
       e.printStackTrace();
     }

     var localeManager = new LocalizationManager(RESOURCES_NAME);
     var windowStorage = new WindowStorage(WINDOW_PATH);

     SwingUtilities.invokeLater(() -> new MainApplicationFrame(localeManager, windowStorage).setVisible(true));
   }}
