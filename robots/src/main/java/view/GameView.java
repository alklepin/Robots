package main.java.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import main.java.model.Entity;
import main.java.model.GameModel;
import main.java.model.Robot;
import main.java.model.Target;

public class GameView extends JPanel {
    private final GameModel gameModel;
    private final Map<Class<?>, GameDrawer> map;

    public GameView(GameModel gameModel) {
        this.gameModel = gameModel;
        this.setDoubleBuffered(true);
        this.map = new HashMap();
        this.map.put((new RobotDrawer()).getDrawingType(), new RobotDrawer());
        this.map.put((new TargetDrawer()).getDrawingType(), new TargetDrawer());
        this.setPreferredSize(new Dimension(600, 600));
        this.setSize(new Dimension(600, 600));
        this.setBorder(BorderFactory.createLineBorder(Color.RED));
    }

    public void updateView() {
        this.onRedrawEvent();
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);
        Iterator var4 = this.gameModel.getEntities().iterator();

        while(var4.hasNext()) {
            Entity entity = (Entity)var4.next();
            ((GameDrawer)this.map.get(entity.getClass())).draw(g2d, entity);
            if (entity instanceof Robot) {
                ((GameDrawer)this.map.get(Target.class)).draw(g2d, ((Robot)entity).getTarget());
            }
        }

    }
}
