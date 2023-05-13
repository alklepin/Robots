package ru.kemichi.robots.gui.windows;

import ru.kemichi.robots.models.Robot;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class PositionWindow extends AbstractWindow{
    private final TextArea textArea = new TextArea();
    private final ResourceBundle bundle;
    private final Robot robot;
    public PositionWindow(ResourceBundle bundle, Robot robot) {
        super(null, bundle.getString("positionWindowHeader"), true, true, true, true);
        this.bundle = bundle;
        this.robot = robot;
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea, BorderLayout.CENTER);
        updateTextArea();
        getContentPane().add(panel);
        robot.addObserver((o, arg) -> updateTextArea());
        pack();
    }

    private void updateTextArea() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String text = bundle.getString("robotPosition") + "\n"
                + decimalFormat.format(robot.getRobotPosition().getX())
                + " "
                + decimalFormat.format(robot.getRobotPosition().getY())
                + "\n"
                + bundle.getString("targetPosition") + "\n"
                + robot.getTargetPosition().getX() + " " + robot.getTargetPosition().getY();
        textArea.setText(text);
    }

    @Override
    public void defaultWindowSetup() {
        this.setLocation(1180, 10);
        setMinimumSize(this.getSize());
        this.pack();
        this.setSize(300, 150);
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}
