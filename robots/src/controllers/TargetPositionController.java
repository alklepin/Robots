package controllers;

import models.TargetModel;

import java.awt.*;

public class TargetPositionController {
    private TargetModel m_model;


    public TargetPositionController(TargetModel model){
        m_model=model;
    }
    public void setTargetPos(Point p){
        m_model.setPos(p.x,p.y);
    }
}
