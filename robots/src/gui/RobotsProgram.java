package gui;

import controllers.TargetPositionController;
import controllers.RobotUpdateController;
import models.RobotModel;
import models.TargetModel;

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
        TargetModel target=new TargetModel(150,100);
        RobotModel model=new RobotModel(100,100,100,target);
        RobotUpdateController updateController = new RobotUpdateController(model);
        TargetPositionController movementController=new TargetPositionController(target);

        MainApplicationFrame frame = new MainApplicationFrame(model, movementController, target);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
      });
    }}
