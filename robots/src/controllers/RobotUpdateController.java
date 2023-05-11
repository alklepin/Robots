package controllers;

import models.RobotModel;

import java.util.Timer;
import java.util.TimerTask;

public class RobotUpdateController {

    private final Timer m_timer = initTimer();
    private RobotModel m_model;
    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    public RobotUpdateController(RobotModel model){
        m_model=model;
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateModel();
            }
        }, 0, 10);
    }

    public void updateModel(){
        m_model.updatePos();
    }
}
