package gui;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

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
    private final ImageView grass;

    private volatile double m_targetPositionX = 150;
    private volatile double m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

    private Bug bug;
    private Target target;

    public GameVisualizer(Pane pane) {
        this.pane = pane;
        grass = loadFile("grass.jpg", this.pane.getWidth(), this.pane.getHeight());
        target = new Target(150, 100, loadFile("apple.png", 30, 30));
        bug = new Bug(100, 100, loadFile("bug_1.png", 60, 60));

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
                bug.X_Position, bug.Y_Position);
        if (distance < 0.5) {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(bug.X_Position, bug.Y_Position, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > bug.Direction) {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < bug.Direction) {
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
        double newX = bug.X_Position + velocity / angularVelocity *
                (Math.sin(bug.Direction + angularVelocity * duration) -
                        Math.sin(bug.Direction));
        if (!Double.isFinite(newX)) {
            newX = bug.X_Position + velocity * duration * Math.cos(bug.Direction);
        }
        double newY = bug.Y_Position - velocity / angularVelocity *
                (Math.cos(bug.Direction + angularVelocity * duration) -
                        Math.cos(bug.Direction));
        if (!Double.isFinite(newY)) {
            newY = bug.Y_Position + velocity * duration * Math.sin(bug.Direction);
        }
        bug.X_Position = newX;
        bug.Y_Position = newY;
        double newDirection = asNormalizedRadians(bug.Direction + angularVelocity * duration);
        bug.Direction = newDirection;
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
        drawRobot(round(bug.X_Position), round(bug.Y_Position), bug.Direction);
        drawTarget(m_targetPositionX, m_targetPositionY);
    }

    private void drawRobot(double x, double y, double direction) {
        double robotCenterX = x - bug.BugSize / 2;
        double robotCenterY = y - bug.BugSize / 2;
        bug.Picture.setX(robotCenterX);
        bug.Picture.setY(robotCenterY);
        bug.Picture.setRotate(90 + direction * 180 / Math.PI);
    }

    private void drawTarget(double x, double y) {
        target.Picture.setX(x - target.TargetSize / 2);
        target.Picture.setY(y - target.TargetSize / 2);
    }
}
