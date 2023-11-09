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

    public static final int FAT = 0;
    public static final int NORMAL = 1;
    public static final int HUNG = 2;

    public static final int MAX_SATIETY = 100;
    public static final int MEDIUM_SATIETY = 50;


    public static TypeRobot randomType() {
        return TypeRobot.values()[(int) (Math.random() * TypeRobot.values().length)];
    }

    public static int getTypeBySatiety(int satiety) {
        if (satiety >= TypeRobot.MAX_SATIETY) {
            return TypeRobot.FAT;
        } else if (satiety >= TypeRobot.MEDIUM_SATIETY) {
            return TypeRobot.NORMAL;
        } else {
            return TypeRobot.HUNG;
        }
    }

    TypeRobot(Color color) {
        this.color = color;
    }


    public Color getColor() {
        return color;
    }
}