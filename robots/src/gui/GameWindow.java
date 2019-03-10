package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;

public class GameWindow extends Pane {


    private final GameVisualizer gameVisualizer;
    public GameWindow(){
        loadFile("grass.jpg", this.getWidth(), this.getHeight());
        loadFile("apple.png", 30, 30);
        loadFile("juke.png", 60, 60);
        this.getChildren().add(new Canvas());
        gameVisualizer = new GameVisualizer(this);
    }

    private void loadFile(String fileName, double width, double height){
        File file = new File(fileName);
        String localUrl = file.toURI().toString();
        Image image = new Image(localUrl, width, height, false, true);
        ImageView picture = new ImageView(image);
        this.getChildren().add(picture);
    }
}
