package gui;

import controllers.ModelPositionController;
import controllers.ModelUpdateController;
import models.RobotModel;

import java.awt.Frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram
{
    public static void main(String[] args) {
      try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } catch (Exception e) {
        e.printStackTrace();
      }
      SwingUtilities.invokeLater(() -> {

        RobotModel model=new RobotModel(100,100,100,150,100);
        ModelUpdateController updateController = new ModelUpdateController(model);
        ModelPositionController movementController=new ModelPositionController(model);

        MainApplicationFrame frame = new MainApplicationFrame(model, movementController);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
      });
    }}
