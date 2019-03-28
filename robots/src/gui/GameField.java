package gui;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class GameField {
    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private Field field;

    private final Canvas canvas;
    private final Pane pane;
    private final ImageView grass;

    public GameField(Pane pane) {
        this.pane = pane;
        grass = loadFile("grass.jpg", this.pane.getWidth(), this.pane.getHeight());

        Target apple = new Target(150, 100);
        this.pane.getChildren().add(apple.Picture);

        Bug juke = new Bug(100, 100);
        this.pane.getChildren().add(juke.Picture);

        Mine[] mines = new Mine[1];
        mines[0] = new Mine(500, 500);
        this.pane.getChildren().add(mines[0].Picture);

        Wall[] walls = new Wall[1];
        walls[0] = new Wall(250, 600);
        this.pane.getChildren().add(walls[0].Picture);

        field = new Field(juke, apple, walls, mines);

        canvas = new Canvas();
        this.pane.getChildren().add(canvas);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                field.onModelUpdateEvent();
            }
        }, 0, 5);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                paint();
            }
        }, 0, 20);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                field.setTargetPosition(e.getX(), e.getY());
            }
        });
    }

    private ImageView loadFile(String fileName, double width, double height){
        File file = new File(fileName);
        String localUrl = file.toURI().toString();
        Image image = new Image(localUrl, width, height, false, true);
        ImageView picture = new ImageView(image);
        this.pane.getChildren().add(picture);
        return picture;
    }

    private void paint() {
        grass.setFitHeight(pane.getHeight());
        grass.setFitWidth(pane.getWidth());
        canvas.setHeight(pane.getHeight());
        canvas.setWidth(pane.getWidth());
        field.draw();
    }
}