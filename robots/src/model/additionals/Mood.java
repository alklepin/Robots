package application.model.additionals;

import java.awt.*;
import java.util.List;
import java.util.Random;

public enum Mood
{
    CALM(Color.GREEN),
    EVIL(Color.RED),
    ENAMORED(Color.MAGENTA),
    HUNGRY(Color.YELLOW),
    DEAD(Color.BLACK);
    private final Color color;
    private static final Random RANDOM = new Random();
    private static final List<Mood> values = List.of(values());

    public static Mood randomMood()
    {
        return values.get(RANDOM.nextInt(values().length - 2));
    }

    Mood(Color color)
    {
        this.color = color;
    }


    public Color getColor() {
        return color;
    }
}
