import gui.MainApplicationFrame;
import localization.LocalizationManager;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {

    private static final String RESOURCES_NAME = "Resources";

    public static void main(String[] args) {
     try {
       UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
     } catch (Exception e) {
       e.printStackTrace();
     }

     var localeManager = new LocalizationManager(RESOURCES_NAME);
     SwingUtilities.invokeLater(() -> new MainApplicationFrame(localeManager).setVisible(true));
   }}
