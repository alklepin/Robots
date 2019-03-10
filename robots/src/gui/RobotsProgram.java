package gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

public class RobotsProgram extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Robots");
        new MainApplicationStage(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
