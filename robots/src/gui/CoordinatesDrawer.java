package gui;

import log.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class CoordinatesDrawer extends JPanel {

    private final java.util.Timer m_timer = initTimer();
    public Parameters parameters;

    private static java.util.Timer initTimer() {
        java.util.Timer timer = new Timer("events generator", true);
        return timer;
    }

    public CoordinatesDrawer(Parameters parameters) {
        this.parameters = parameters;
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
    }


    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawString("X: " + String.valueOf(parameters.m_robotPositionX), 25, 25);
        g2d.drawString("Y: " + String.valueOf(parameters.m_robotPositionY), 25, 45);
        g2d.drawString("DIRECTION: " + String.valueOf(parameters.m_robotDirection), 25, 65);
        g2d.drawString("Target X: " + String.valueOf(parameters.m_targetPositionX), 25, 85);
        g2d.drawString("Target Y: " + String.valueOf(parameters.m_targetPositionY), 25, 105);
        setDoubleBuffered(true);
    }


}
