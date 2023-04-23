package org.iffomko.gui;

import org.iffomko.messagedFormatCached.MessageFormatCached;
import org.iffomko.models.Robot;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

/**
 * <p>Окно, которое содержит в себе актуальные координаты робота</p>
 */
public class RobotPositionPanel extends JPanel implements Observer {
    private final JTextArea textField;
    private String text = "";
    private final Robot robot;

    /**
     * @param robot - модель робота, за которой мы наблюдаем
     */
    public RobotPositionPanel(Robot robot) {
        textField = new JTextArea();
        textField.setEditable(false);
        textField.setBackground(Color.getColor("#333"));
        textField.setVisible(true);

        this.robot = robot;

        Font textFieldFont = new Font("Arial", Font.BOLD, 14);

        textField.setFont(textFieldFont);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(textField, BorderLayout.CENTER);

        add(textPanel);
    }

    /**
     * <p>Этот метод вызывается каждый раз, когда наблюдаемый объект изменяется</p>
     * <p>
     *     В программе этот метод вызывается тогда, когда наблюдаемый объект, который наследуется от <code>Observable</code>,
     *     вызывает метод <code>notifyObservers</code>
     * </p>
     * @param o   - наблюдаемый объект, который уведомил об изменениях
     * @param arg - аргумент, который был положен при вызове метода <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o == null) {
            return;
        }

        if (robot.equals(o)) {
            if (Robot.KEY_ROBOT_POSITION_CHANGED.equals(arg)) {
                onRobotPositionChanged();
            }
        }
    }

    /**
     * <p>Метод, который обрабатывает событие изменения позиции робота</p>
     */
    private void onRobotPositionChanged() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "org.iffomko.gui.localizationProperties.robotPositionPanel.RobotPositionPanelResource",
                new Locale(System.getProperty("user.language"), System.getProperty("user.country"))
        );

        String pattern = "X: {0}, Y: {1}, {2}: {3}";
        Object[] params = {
                (int)robot.getX(),
                (int)robot.getY(),
                resourceBundle.getString("direction"),
                ((int) (robot.getDirection() * 180 / Math.PI))
        };

        String text = MessageFormatCached.format(pattern, params);

        textField.setText(text);
    }
}
