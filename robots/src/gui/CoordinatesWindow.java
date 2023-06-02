package gui;


import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CoordinatesWindow extends JInternalFrame implements Observer {

    private CoordinatesDrawer drawer;

    public CoordinatesWindow(Parameters parameters) {
        super("Текущие координаты робота", true, true, true, true);


        drawer = new CoordinatesDrawer(parameters);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(drawer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

    }

    @Override
    public void update(Observable o, Object arg) {
        var t = (Parameters) o;
        drawer.parameters = t;

    }
}