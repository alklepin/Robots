package controllers;

import models.RobotModel;
import models.TargetModel;

import java.awt.*;

public class ModelPositionController {
    private TargetModel m_model;


    public ModelPositionController(TargetModel model){
        m_model=model;
    }
    public void setTargetPos(Point p){
        m_model.setPos(p.x,p.y);
    }
}
