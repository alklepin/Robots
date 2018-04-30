package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    


    void addObs(RobotCoordWindow window){//добавление наблюдателей
        m_visualizer.robot.observable.add(window);
    }

    JInternalFrame getObserver(){//получить объект наблюдателя
        return  (JInternalFrame) m_visualizer.robot.observable.get(0);
    }

    boolean isNoObs(){//проверка на существование наблюдателей
        return  m_visualizer.robot.observable.isEmpty();
    }

    void removeObs(RobotCoordWindow window){//удаление наблюдателей
        m_visualizer.robot.observable.remove(window);
    }

    Point getRobotPosition(){
        return new Point(m_visualizer.robot.m_robotPositionX,m_visualizer.robot.m_robotPositionY);
    }

    int getDirection(){
        return m_visualizer.robot.m_robotDirection;
    }

    void setDirection(int direction){
        m_visualizer.robot.m_robotDirection = direction;
    }

    void setRobotPosition(Point p){
        m_visualizer.robot.setRobotPosition(p);
    }

    Point getTargetPosition(){
        return m_visualizer.robot.getTargetPosition();
    }

    int getTargetX() {
        return m_visualizer.robot.m_targetPositionX;
    }

    int getTargetY(){
        return m_visualizer.robot.m_targetPositionY;
    }

    void setTargetPosition(Point position){
        m_visualizer.robot.setTargetPosition(position);
    }
}

