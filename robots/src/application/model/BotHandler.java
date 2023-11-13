package application.model;

import java.awt.*;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.awt.geom.Point2D.distance;

public class BotHandler
{
    private final Dimension dimension;
    private final java.util.List<Bot> bots;
    private final List<Food> foods;
    private final PropertyChangeSupport support;
    private static final int BOT_COUNT = 5;
    private static final int FOOD_COUNT = 5;
    private static final int START_LIFETIME = 40;
    private static final int FULL_LIFETIME = 90;

    public BotHandler()
    {
        this.bots = new ArrayList<>();
        this.foods = new ArrayList<>();
        this.dimension = new Dimension(400, 400);
        this.support = new PropertyChangeSupport(this);

        for (int i = 0; i < FOOD_COUNT; i++)
        {
            Food food = new Food((int) (Math.random() * 400), (int) (Math.random() * 400));
            foods.add(food);
        }

        for (int i = 0; i < BOT_COUNT; i++)
        {
            Bot bot = new Bot(Math.random() * 400, Math.random() * 400, (int) (START_LIFETIME + Math.random() * (FULL_LIFETIME - START_LIFETIME)));
            bot.setFoodGoal(findFood(bot));
            bots.add(bot);
            bot.onStart(support);
        }
    }

    private Food findFood(Bot bot)
    {
        double minDistance = Double.MAX_VALUE;
        Food nearestFood = null;

        for (Food food : foods)
        {
            double distance = distance(bot.getPositionX(), bot.getPositionY(), food.getX(), food.getY());

            if (distance < minDistance)
            {
                minDistance = distance;
                nearestFood = food;
            }
        }

        if (nearestFood != null)
        {
            foods.remove(nearestFood);
        }

        return nearestFood;
    }

    public void suddenBotClone()
    {
        if (Math.random() * 6 > 2)
        {
            Random rand = new Random();
            Bot bot = bots.get(rand.nextInt(bots.size()));
            double x = bot.getPositionX();
            double y = bot.getPositionY();
            bot.onFinish(support);
            bots.remove(bot);
            for (int i = 0; i < 2; i++)
            {
                int xx = (int) (Math.random() * dimension.width);
                int yy = (int) (Math.random() * dimension.height);
                Food food = new Food(xx, yy);
                foods.add(food);
                Bot b = new Bot(x, y, bot.getLifeTime() / 2);
                b.setFoodGoal(findFood(b));
                bots.add(b);
            }
        }
    }

    public void changeLifeTime()
    {
        support.firePropertyChange("change life time", null, -10);
    }

    public List<Bot> getBotList()
    {
        return bots;
    }

    public List<Food> getFoodList()
    {
        return foods;
    }

    public void setFoodGoal(Point point)
    {
        support.firePropertyChange("new point", null, point);
    }

    public void update()
    {
        for (Food food : foods){
            if (!food.isPositionCorrect(dimension))
            {
                food.setX((int) (Math.random() * dimension.width));
                food.setY((int) (Math.random() * dimension.height));
            }
        }
        for (Bot bot : bots){
            Food f = bot.getFoodGoal();
            if (!f.spawn)
            {
                int xx = (int) (Math.random() * dimension.width);
                int yy = (int) (Math.random() * dimension.height);
                Food food = new Food(xx, yy);
                foods.add(food);
                bot.setFoodGoal(findFood(bot));
            }
            bot.update();
        }
    }
}
