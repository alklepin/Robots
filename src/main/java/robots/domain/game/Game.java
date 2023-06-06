package robots.domain.game;

import robots.domain.events.Event;
import robots.interfaces.GameObject;

import java.awt.*;
import java.util.Observable;

public class Game {

    private final GameObject[] gameObjects;

    public Game(GameObject... gameObjects) {
        this.gameObjects = gameObjects;
    }

    public void draw(Graphics2D g) {
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(g);
        }
    }

    public void update() {
        for (GameObject gameObject : gameObjects) {
            gameObject.makeMove();
        }
    }

    public void handleEvent(Event event) {
        for (GameObject gameObject : gameObjects) {
            gameObject.handleEvent(event);
        }
    }
}
