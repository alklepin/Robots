package application.view;

import application.model.Entity;
import application.model.GameModel;
import application.model.Target;
import application.view.draw.Drawer;
import application.view.draw.BacteriaDrawer;
import application.view.draw.TargetDrawer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameView extends JPanel
{
    private final GameModel gameModel;
    private final Map<Class<?>, Drawer> map;

    public GameView(GameModel gameModel)
    {
        map = new HashMap<>();
        map.put(new BacteriaDrawer().getDrawingType(), new BacteriaDrawer());
        map.put(new TargetDrawer().getDrawingType(), new TargetDrawer());
        this.gameModel = gameModel;
        setDoubleBuffered(true);
        this.setPreferredSize(new Dimension(400, 400));
        this.setSize(new Dimension(400, 400));
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
    }

    public void updateView()
    {
        onRedrawEvent();
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        ArrayList<Entity> entities = (ArrayList<Entity>) gameModel.getEntities();
        for (Entity entity : entities)
        {
            map.get(entity.getClass()).draw(g2d, entity);
            map.get(Target.class).draw(g2d, entity);
        }
    }
}
