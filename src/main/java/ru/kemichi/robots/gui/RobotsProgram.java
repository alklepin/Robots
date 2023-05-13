package ru.kemichi.robots.gui;

import ru.kemichi.robots.gui.windows.GameWindow;
import ru.kemichi.robots.gui.windows.LogWindow;
import ru.kemichi.robots.gui.windows.PositionWindow;
import ru.kemichi.robots.log.Logger;
import ru.kemichi.robots.models.Robot;

import java.awt.Frame;
import java.util.ResourceBundle;

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
      Robot robot = new Robot();
      SwingUtilities.invokeLater(() -> {
        ResourceBundle bundle = ResourceBundle.getBundle("resources");
        MainApplicationFrame frame = new MainApplicationFrame(bundle, 50,
                new GameWindow(bundle, robot),
                new LogWindow(Logger.getDefaultLogSource(), bundle),
                new PositionWindow(bundle, robot));
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
      });
    }}
