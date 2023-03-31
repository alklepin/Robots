package org.iffomko.gui;

import org.iffomko.models.Robot;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * <p>Окно, которое содержит в себе актуальные координаты робота</p>
 */
public class ActualRobotPosition extends JPanel implements Observer {
    private final JTextArea textField;
    private String text = "";
    private final Robot robot;

    /**
     * @param robot - модель робота, за которой мы наблюдаем
     */
    public ActualRobotPosition(Robot robot) {
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
        text = "x: " + ((int)robot.getX()) + ", y: " + ((int)robot.getY() + ", direction: " + ((int) (robot.getDirection() * 180 / Math.PI)));
        textField.setText(text);
    }
}
