package gui;

import javafx.scene.layout.Pane;


public class GameWindow extends Pane {

    private final GameVisualizer gameVisualizer;

    public GameWindow(){
        gameVisualizer = new GameVisualizer(this);
    }
}
