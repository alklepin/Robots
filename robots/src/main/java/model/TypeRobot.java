package model;

import java.awt.*;
import java.util.List;
import java.util.Random;

public enum TypeRobot {
    CALM(Color.GREEN),
    HUNGRY(Color.RED),
    DEAD(Color.BLACK);
    private final Color color;
    private static final Random RANDOM = new Random();
    private static final List<TypeRobot> values = List.of(values());

    public static TypeRobot randomType() {
        return values.get(RANDOM.nextInt(values().length - 2));
    }

    TypeRobot(Color color) {
        this.color = color;
    }


    public Color getColor() {
        return color;
    }
}