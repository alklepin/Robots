package controllers;

import models.RobotModel;

import java.awt.*;

public class ModelPositionController {
    private RobotModel m_model;


    public ModelPositionController(RobotModel model){
        m_model=model;
    }
    public void setTargetPos(Point p){
        m_model.setTargetPosition(p);
    }
}
