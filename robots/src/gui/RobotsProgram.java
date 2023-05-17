package gui;

import config.ConfigReader;
import config.ConfigWriter;
import config.PredeterminedPathConfigReader;
import config.PredeterminedPathConfigWriter;
import controllers.TargetPositionController;
import controllers.RobotUpdateController;
import gui.serial.MainWindowStateContainer;
import log.LogWindowSource;
import log.Logger;
import log.states.LoggerSourceState;
import models.RobotModel;
import models.TargetModel;
import models.states.RobotState;
import models.states.TargetState;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {
    private static File m_serialize_path = new File("C:\\Users\\as-pa\\config.conf");
    private static TargetModel m_targetModel;
    private static RobotModel m_robotModel;
    private static RobotUpdateController m_updater;
    private static LogWindowSource m_logs;
    private static TargetPositionController m_targetController;
    private static MainApplicationFrame m_frame;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {


            try (PredeterminedPathConfigReader reader = new PredeterminedPathConfigReader(m_serialize_path.getPath())) {
                readProgramState(reader);

            } catch (IOException | ClassNotFoundException e) {
                initProgramState();

            }
        });
    }
    private static void initProgramState(){
        m_targetModel = new TargetModel(150, 100);
        m_robotModel = new RobotModel(100, 100, 100, m_targetModel);
        m_updater = new RobotUpdateController(m_robotModel);
        m_logs= Logger.getDefaultLogSource();
        m_targetController = new TargetPositionController(m_targetModel);
        m_frame = new MainApplicationFrame(m_robotModel, m_targetController, m_targetModel,m_logs);
        m_frame.pack();
        m_frame.setVisible(true);
        m_frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        m_frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                synchronized (this){
                    try(ConfigWriter writer = new PredeterminedPathConfigWriter(m_serialize_path.getPath())){
                        writeProgramState(writer);

                    } catch (IOException ex) {
                        ex.printStackTrace();

                    }
                    finally {
                        super.windowClosing(e);
                    }
                }


            }
        });
    }
    private static void readProgramState(ConfigReader reader) throws IOException, ClassNotFoundException {
        MainWindowStateContainer Windowscontainer=(MainWindowStateContainer)reader.readObject();
        RobotState robotContainer=(RobotState) reader.readObject();
        TargetState targetContainer=(TargetState) reader.readObject();
        LoggerSourceState logs=(LoggerSourceState)reader.readObject();
        m_logs=new LogWindowSource(logs);
        m_targetModel=new TargetModel(targetContainer);
        m_robotModel=new RobotModel(robotContainer,m_targetModel);
        m_targetController=new TargetPositionController(m_targetModel);
        m_updater=new RobotUpdateController(m_robotModel);
        m_frame=new MainApplicationFrame(m_robotModel,m_targetController,m_targetModel, Windowscontainer,m_logs);
        m_frame.pack();
        m_frame.setVisible(true);
        m_frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        m_frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                synchronized (this){
                    try(ConfigWriter writer = new PredeterminedPathConfigWriter(m_serialize_path.getPath())){
                        writeProgramState(writer);

                    } catch (IOException ex) {
                        ex.printStackTrace();

                    }
                    finally {
                        super.windowClosing(e);
                    }
                }


            }
        });
    }

    private static void writeProgramState(ConfigWriter writer) throws IOException{
        MainWindowStateContainer windowContainer=m_frame.getFrameState();
        RobotState robotContainer=m_robotModel.getState();
        TargetState targetContainer=m_targetModel.getState();
        LoggerSourceState logs=m_logs.getState();
        writer.writeObject(windowContainer);
        writer.writeObject(robotContainer);
        writer.writeObject(targetContainer);
        writer.writeObject(logs);
    }
}
