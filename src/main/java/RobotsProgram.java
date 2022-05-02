import gui.MainApplicationFrame;
import localization.LocalizationManager;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {

   public static void main(String[] args) {
     try {
       UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//       UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//       UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
     } catch (Exception e) {
       e.printStackTrace();
     }

     SwingUtilities.invokeLater(() -> {
       SwingUtilities.invokeLater(() -> new MainApplicationFrame(LocalizationManager.getInstance()).setVisible(true));
     });
   }}
