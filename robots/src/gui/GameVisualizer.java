package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class GameVisualizer {
    private final Timer m_timer = initTimer();

    private static Timer initTimer() {
        Timer timer = new Timer("events generator", true);
        return timer;
    }


    private final Canvas canvas;
    private final Pane pane;
    private final ImageView bug;
    private final ImageView apple;
    private final ImageView grass;

    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

    private volatile double m_targetPositionX = 150;
    private volatile double m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public GameVisualizer(Pane pane) {
        this.pane = pane;
        grass = (ImageView) pane.getChildren().get(0);
        apple = (ImageView) pane.getChildren().get(1);
        bug = (ImageView) pane.getChildren().get(2);
        canvas = (Canvas) pane.getChildren().get(3);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 5);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                paint(canvas.getGraphicsContext2D());
            }
        }, 0, 20);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                setTargetPosition(e.getX(), e.getY());
            }
        });
    }

    protected void setTargetPosition(double x, double y) {
        m_targetPositionX = x;
        m_targetPositionY = y;
    }

    private static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    protected void onModelUpdateEvent() {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection) {
            angularVelocity = -maxAngularVelocity;
        }

        moveRobot(velocity, angularVelocity, 10);
    }

    private static double applyLimits(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private void moveRobot(double velocity, double angularVelocity, double duration) {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX)) {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY)) {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;
    }

    private static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    private static int round(double value) {
        return (int) (value + 0.5);
    }

    private void paint(GraphicsContext gc) {
        grass.setFitHeight(pane.getHeight());
        grass.setFitWidth(pane.getWidth());
        canvas.setHeight(pane.getHeight());
        canvas.setWidth(pane.getWidth());
        drawRobot(round(m_robotPositionX), round(m_robotPositionY), m_robotDirection);
        drawTarget(m_targetPositionX, m_targetPositionY);
    }

    private void drawRobot(double x, double y, double direction) {
        double robotCenterX = x - 30;
        double robotCenterY = y - 30;
        bug.setX(robotCenterX);
        bug.setY(robotCenterY);
        bug.setRotate(90 + direction * 180 / Math.PI);
    }

    private void drawTarget(double x, double y) {
        apple.setX(x - 15);
        apple.setY(y - 15);
    }
}
