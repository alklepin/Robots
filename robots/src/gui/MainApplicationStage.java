package gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainApplicationStage {

    public MainApplicationStage(Stage primaryStage){
        Pane gameWindow = new GameWindow();
        Pane logWindow = new Pane();

        HashMap<String , Pane> panes = new HashMap<String, Pane>();
        panes.put("GameWindow", gameWindow);
        panes.put("LogWindow", logWindow);


        ObservableList<String> langs = FXCollections.observableArrayList("GameWindow", "LogWindow");
        ChoiceBox<String> choiceBox = new ChoiceBox<String>(langs);


        choiceBox.setValue("GameWindow");
        gameWindow.getChildren().add(choiceBox);

        HashMap<String , Scene> scenes = new HashMap<String , Scene>();
        scenes.put("GameWindow", new Scene(gameWindow));
        scenes.put("LogWindow", new Scene(logWindow));

        choiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.setScene(scenes.get(choiceBox.getValue()));
                panes.get(choiceBox.getValue()).getChildren().add(choiceBox);
                primaryStage.setFullScreen(true);
            }
        });


        primaryStage.setScene(scenes.get("GameWindow"));
        primaryStage.setFullScreen(true);

    }
}
