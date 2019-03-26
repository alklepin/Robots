package gui;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.ArrayList;
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
    private ImageView showedBug;
    private final ArrayList<ImageView> bugs = new ArrayList<>();
    private final ImageView apple;
    private final ImageView grass;

    private final double bugSize = 60;
    private final double appleSize = 30;

    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

    private volatile double m_targetPositionX = 150;
    private volatile double m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    public GameVisualizer(Pane pane) {
        this.pane = pane;
        grass = loadFile("grass.jpg", this.pane.getWidth(), this.pane.getHeight());
        apple = loadFile("apple.png", appleSize, appleSize);
        bugs.add(loadFile("bug_1.png", bugSize, bugSize));
        bugs.add(loadFile("bug_2.png", bugSize, bugSize));
        showedBug = bugs.get(0);

        canvas = new Canvas();
        this.pane.getChildren().add(canvas);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onModelUpdateEvent();
            }
        }, 0, 5);
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                paint();
            }
        }, 0, 20);

        this.canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                setTargetPosition(e.getX(), e.getY());
            }
        });
    }

    private ImageView loadFile(String fileName, double width, double height){
        File file = new File(fileName);
        String localUrl = file.toURI().toString();
        Image image = new Image(localUrl, width, height, false, true);
        ImageView picture = new ImageView(image);
        this.pane.getChildren().add(picture);
        return picture;
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

    private void paint() {
        grass.setFitHeight(pane.getHeight());
        grass.setFitWidth(pane.getWidth());
        canvas.setHeight(pane.getHeight());
        canvas.setWidth(pane.getWidth());
        drawRobot(round(m_robotPositionX), round(m_robotPositionY), m_robotDirection);
        drawTarget(m_targetPositionX, m_targetPositionY);
    }


    private int index = 0;
    private void drawRobot(double x, double y, double direction) {
        double robotCenterX = x - bugSize/2;
        double robotCenterY = y - bugSize/2;
        index++;
        if (index == 10){
            setNextBug();
            index = 0;
        }
        showedBug.setX(robotCenterX);
        showedBug.setY(robotCenterY);
        showedBug.setRotate(90 + direction * 180 / Math.PI);
    }


    private void setNextBug(){
        showedBug.setVisible(false);
        int i = bugs.indexOf(showedBug);
        i++;
        i %= bugs.size();
        showedBug = bugs.get(i);
        showedBug.setVisible(true);
    }

    private void drawTarget(double x, double y) {
        apple.setX(x - appleSize/2);
        apple.setY(y - appleSize/2);
    }
}
