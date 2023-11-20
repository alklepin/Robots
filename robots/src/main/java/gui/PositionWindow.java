package main.java.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import main.java.model.Robot;

public class PositionWindow extends JInternalFrame implements Observer, Translatable {
    private final JLabel labelX;
    private final JLabel labelY;
    private final Robot robot;

    public PositionWindow(Robot robot, int width, int height) {
        super("Координаты робота", true, true, true, true);
        this.robot = robot;
        JPanel panel = new JPanel(new BorderLayout());
        robot.addObserver(this);
        this.labelX = new JLabel("X: %f".formatted(robot.getPositionX()));
        this.labelY = new JLabel("Y: %f".formatted(robot.getPositionY()));
        panel.add(this.labelX, "First");
        panel.add(this.labelY, "Center");
        this.getContentPane().add(panel);
        this.pack();
        this.setSize(width, height);
        this.setLocation(1600, 50);
    }

    public void update(Observable o, Object arg) {
        if (arg.equals("The robot's position has changed")) {
            EventQueue.invokeLater(this::updateCoords);
        }

    }

    public void updateCoords() {
        this.labelX.setText("X: %f".formatted(this.robot.getPositionX()));
        this.labelY.setText("Y: %f".formatted(this.robot.getPositionY()));
    }

    public void setTranslate(ResourceBundle bundle) {
        this.setTitle(bundle.getString("positionWindowHeader"));
    }
}
