package gui;

import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CoordinatesWindow extends JInternalFrame implements Observer
{
    private TextArea coordinatesArea;

    public CoordinatesWindow()
    {
        super("Координаты робота", true, true, true, true);

        coordinatesArea = new TextArea();
        coordinatesArea.setSize(100, 50);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(coordinatesArea, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    private void updateDisplayedRobotCoordinates(Pair<Double, Double> newCoordinates)
    {
        StringBuilder coordinatesAreaText = new StringBuilder();
        coordinatesAreaText.append("X: ").append(newCoordinates.getKey()).append("\nY: ").append(newCoordinates.getValue());
        coordinatesArea.setText(coordinatesAreaText.toString());
        coordinatesArea.invalidate();
    }

    @Override
    public void update(Observable observable, Object o)
    {
        updateDisplayedRobotCoordinates((Pair<Double, Double>) o);  //yep, a cast, but the standard Observer interface's
        //update() can only accept Objects
    }
}