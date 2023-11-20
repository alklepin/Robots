package main.java.model;

import java.awt.Color;
import java.util.List;
import java.util.Random;

public enum TypeRobot {
    CALM(Color.GREEN),
    HUNGRY(Color.RED),
    DEAD(Color.BLACK);

    private final Color color;
    private static final Random RANDOM = new Random();
    private static final List<TypeRobot> VALUES = List.of(values());

    public static TypeRobot randomType() {
        return (TypeRobot)VALUES.get(RANDOM.nextInt(values().length - 2));
    }

    private TypeRobot(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }
}
