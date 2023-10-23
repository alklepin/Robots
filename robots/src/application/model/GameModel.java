package application.model;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameModel
{
    private final BotHandler botHandler;

    public GameModel()
    {
        this.botHandler = new BotHandler();

        Timer timer = initTimer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("timer");
                botHandler.changeLifeTime();
                botHandler.suddenBotClone();
            }
        }, 0, 3000);
    }

    private static java.util.Timer initTimer()
    {
        return new Timer("events generator", true);
    }

    public void setDimension(Dimension dimension)
    {
        for (Bot bot : botHandler.getBotList())
            bot.setDimension(dimension);
    }

    public Dimension getDimension()
    {
        return botHandler.getBotList().get(0).getDimension();
    }

    public void updateModel()
    {
        botHandler.update();
    }

    public List<Bot> getRobots()
    {
        return botHandler.getBotList();
    }

    public List<Food> getFoods()
    {
        return botHandler.getFoodList();
    }

    public void setFoodGoal(Point point)
    {
        botHandler.setFoodGoal(point);
    }
}
