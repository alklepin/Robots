package application.model.additionals;

import java.awt.*;
import java.util.List;
import java.util.Random;

public enum Condition
{
    CALM(Color.GREEN),
    HUNGRY(Color.RED),
    DEAD(Color.BLACK);
    private final Color color;
    private static final Random RANDOM = new Random();
    private static final List<Condition> values = List.of(values());

    public static Condition randomCondition()
    {
        return values.get(RANDOM.nextInt(values().length - 2));
    }

    Condition(Color color)
    {
        this.color = color;
    }


    public Color getColor() {
        return color;
    }
}
