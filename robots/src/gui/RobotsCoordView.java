package gui;

import serialization.SerializationInternalFrame;

import java.util.TimerTask;
import javax.swing.JInternalFrame;
import java.util.Timer;
import javax.swing.JTextArea;

public class RobotsCoordView extends SerializationInternalFrame {


    public RobotsCoordView(GameWindow gameWindow) {
        super("Координаты", true, true, true, true);
        JTextArea area = new JTextArea();
        add(area);
        Timer timer = new Timer("Shower coordinates", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                area.setText("X = " + (int) gameWindow.getM_visualizer().getM_robotPositionX()
                        + " \tY = " + (int) gameWindow.getM_visualizer().getM_robotPositionY());

            }
        }, 0, 5);
    }
}
