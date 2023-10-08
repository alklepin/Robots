package application.model;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.List;

public class GameModel
{
    private final List<Bot> bots;
    private final PropertyChangeSupport support;
    private static final int BOT_COUNT = 5;

    public GameModel()
    {
        this.bots = new ArrayList<>();
        this.support = new PropertyChangeSupport(this);

        for (int i = 0; i < BOT_COUNT; i++)
        {
            Bot bot = new Bot(Math.random() * 400, Math.random() * 400);
            bots.add(bot);
            addPropertyChangeListener(bot);
        }

        Timer timer = initTimer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("timer");
                support.firePropertyChange("change life time", null, -10);
                if (Math.random() * 4 > 2.3)
                    suddenBotClone();
            }
        }, 0, 3000);
    }

    private static java.util.Timer initTimer()
    {
        return new Timer("events generator", true);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setDimension(Dimension dimension)
    {
        for (Bot robot : bots)
            robot.setDimension(dimension);
    }

    public Dimension getDimension()
    {
        return bots.get(0).getDimension();
    }

    public void updateModel()
    {
        for (Bot bot : bots)
            bot.update();
    }

    private void suddenBotClone()
    {
        Random rand = new Random();
        Bot bot = bots.get(rand.nextInt(bots.size()));
        double x = bot.getPositionX();
        double y = bot.getPositionY();
        removePropertyChangeListener(bot);
        bots.remove(bot);
        for (int i = 0; i < 2; i++)
            bots.add(new Bot(x, y));
    }

    public List<Bot> getRobots()
    {
        return bots;
    }

    public void setFoodGoal(Point point)
    {
        support.firePropertyChange("new point", null, point);
    }
}
