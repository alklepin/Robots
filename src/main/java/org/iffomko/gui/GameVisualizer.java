package org.iffomko.gui;

import org.iffomko.gui.views.RobotVisualizer;
import org.iffomko.gui.views.TargetVisualizer;
import org.iffomko.gui.models.Robot;
import org.iffomko.gui.models.Target;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

/**
 * Основная логика игры
 */
public class GameVisualizer extends JPanel implements Observer
{
    private final Robot robot;
    private final Target target;
    private final RobotVisualizer robotVisualizer;
    private final TargetVisualizer targetVisualizer;
    private final int duration;

    public GameVisualizer(Robot robot, Target target, int duration)
    {
        this.robot = robot;
        this.robotVisualizer = new RobotVisualizer(robot);
        this.targetVisualizer = new TargetVisualizer(target);
        this.target = target;
        this.duration = duration;

        setDoubleBuffered(true);
    }

    /**
     * Добавляет в очередь событий перерисовку игры
     */
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    /**
     * Метод, который вызывается каждый раз для обновления координат моделей
     */
    protected void onModelUpdateEvent()
    {
        robot.move(target.getX(), target.getY(), duration);
    }

    /**
     * Отрисовывает графику игры
     * @param g -  <code>Graphics</code> контекст, в котором нужно отрисовывать игру
     */
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        robotVisualizer.paint(g2d);
        targetVisualizer.paint(g2d);
    }

    /**
     * <p>Метод, который вызывается каждый раз для перерисовки, когда изменяется позиция робота</p>
     */
    private void onRobotPositionChanged() {
        onRedrawEvent();
    }

    /**
     * <p>Метод, который вызывается каждый раз для перерисовки, когда изменяется позиция точки назначения</p>
     */
    private void onTargetPositionChanged() {
        onRedrawEvent();
    }

    /**
     * <p>Этот метод вызывается каждый раз, когда наблюдаемый объект изменяется</p>
     * <p>
     *     В программе этот метод вызывается тогда, когда наблюдаемый объект, который наследуется от <code>Observable</code>,
     *     вызывает метод <code>notifyObservers</code>
     * </p>
     * @param o   - наблюдаемый объект, который уведомил об изменениях
     * @param arg - аргумент, который был положен при вызове метода <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        if (robot.equals(o)) {
            if (Robot.KEY_ROBOT_POSITION_CHANGED.equals(arg)) {
                onRobotPositionChanged();
            }
        }
        if (target.equals(o)) {
            if (Target.KEY_TARGET_POSITION_CHANGED.equals(arg)) {
                onTargetPositionChanged();
            }
        }
    }
}