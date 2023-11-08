package main.java.view;

import model.Entity;
import model.Model;
import model.Target;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class GameView extends JPanel {
    private final Model gameModel;
    private final Map<Class<?>, GameDrawer> map;
    public GameView(Model gameModel) {
        this.gameModel = gameModel;
        setDoubleBuffered(true);
        map = new HashMap<>();
        map.put(new RobotDrawer().getDrawingType(), new RobotDrawer());
        map.put(new TargetDrawer().getDrawingType(), new TargetDrawer());
        this.setPreferredSize(new Dimension(600, 600));
        this.setSize(new Dimension(600, 600));
        this.setBorder(BorderFactory.createLineBorder(Color.RED));

    }

    public void updateView() {
        onRedrawEvent();
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        ArrayList<Entity> entities = (ArrayList<Entity>) gameModel.getEntities();
        for (Entity entity : entities) {
            map.get(entity.getClass()).draw(g2d, entity);
            map.get(Target.class).draw(g2d, entity);
        }
    }
}