package application.view;

import application.model.Bot;
import application.model.GameModel;
import application.view.draw.BotDrawer;
import application.view.draw.FoodDrawer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameView extends JPanel
{
    private final GameModel gameModel;
    BotDrawer botDrawer;
    FoodDrawer foodDrawer;

    public GameView(GameModel gameModel)
    {
        botDrawer = new BotDrawer();
        foodDrawer = new FoodDrawer();
        this.gameModel = gameModel;
        setDoubleBuffered(true);
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
        ArrayList<Bot> bots = (ArrayList<Bot>) gameModel.getRobots();
        for (Bot bot : bots)
        {
            botDrawer.draw(g2d, bot);
            foodDrawer.draw(g2d, bot.getFoodGoal());
        }
    }
}
