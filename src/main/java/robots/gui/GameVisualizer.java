package robots.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import robots.domain.game.Game;

public class GameVisualizer extends JPanel {
    private final Timer m_timer = initTimer();
    private Game game;

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public GameVisualizer(Game game) {
        this.game = game;
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.update();
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                System.out.println("game (mouse): " + point);
                game.handleEvent(new robots.domain.events.MouseEvent(point.x * 2, point.y * 2));
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        game.draw((Graphics2D) g);
    }
}
