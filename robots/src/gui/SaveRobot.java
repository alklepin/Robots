package gui;

import java.awt.*;
import java.io.Serializable;

public class SaveRobot implements Serializable {
    Point position;
    Point aim;
    int orientation;

    private SaveRobot(){}

    public SaveRobot(Point position, Point aim, int orientation){
        this.position = position;
        this.aim = aim;
        this.orientation = orientation;
    }

    public SaveRobot(GameWindow gameWindow){
        this.position = gameWindow.getRobotPosition();
        this.aim = gameWindow.getTargetPosition();
        this.orientation = gameWindow.getDirection();
    }

}
