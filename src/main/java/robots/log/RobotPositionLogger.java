package robots.log;

import robots.domain.Robot;

import java.util.Observable;
import java.util.Observer;

public class RobotPositionLogger implements Observer {
    private final LogWindowSource logSource;

    public RobotPositionLogger(LogWindowSource logSource) {
        this.logSource = logSource;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Robot) {
            Robot robot = (Robot) o;
            logSource.append(LogLevel.Info, "Robot position: " + robot.getRobotPosition().toString());
        }
    }
}
