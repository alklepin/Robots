package serviceLocators;

import controllers.TargetPositionController;
import log.LogWindowSource;
import log.Logger;
import log.states.LoggerSourceState;
import models.RobotModel;
import models.TargetModel;
import models.states.RobotState;
import models.states.TargetState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ModelAndControllerLocator {
    private RobotModel m_robotModel;
    private TargetModel m_targetModel;
    private TargetPositionController m_targetController;
    private LogWindowSource m_logs;
    public ModelAndControllerLocator(RobotModel robotModel, TargetModel targetModel, TargetPositionController targetController, LogWindowSource m_logs) {
        this.m_robotModel = robotModel;
        this.m_targetModel = targetModel;
        this.m_targetController = targetController;
        this.m_logs = m_logs;
    }
    public static ModelAndControllerLocator getDefault(){
        var targetModel = new TargetModel(150, 100);
        var robotModel = new RobotModel(100, 100, 100, targetModel);
        var logs = Logger.getDefaultLogSource();
        var targetController = new TargetPositionController(targetModel);
        return new ModelAndControllerLocator(robotModel, targetModel, targetController, logs);
    }
    public static ModelAndControllerLocator getFromConfig(ObjectInputStream reader) throws IOException, ClassNotFoundException {
        RobotState robotState=(RobotState)reader.readObject();
        TargetState targetState=(TargetState)reader.readObject();
        TargetModel targetModel=new TargetModel(targetState);
        RobotModel robotModel=new RobotModel(robotState,targetModel);

        var log=(LoggerSourceState)reader.readObject();
        LogWindowSource logs= Logger.getDefaultLogSource();
        for (var a: log.logs) {
            logs.append(a.getLevel(),a.getMessage());
        }
        var TargetController=new TargetPositionController(targetModel);
return new ModelAndControllerLocator(robotModel, targetModel, TargetController, logs);
    }
    public void writeStateToConfig(ObjectOutputStream writer) throws IOException {
        writer.writeObject(m_robotModel.getState());
        writer.writeObject(m_targetModel.getState());
        writer.writeObject(m_logs.getState());
    }
    public RobotModel getRobotModel(){return m_robotModel;};
    public TargetModel getTargetModel(){return m_targetModel;}
    public TargetPositionController getTargetPositionController(){return m_targetController;}
    public LogWindowSource getLogSource(){
        return m_logs;
    }
}
