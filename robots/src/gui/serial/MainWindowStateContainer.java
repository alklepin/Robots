package gui.serial;

import java.io.Serializable;

public class MainWindowStateContainer implements Serializable {
    public InnerWindowStateContainer GameState;
    public InnerWindowStateContainer LoggerState;

    public MainWindowStateContainer(InnerWindowStateContainer gameState, InnerWindowStateContainer loggerState, InnerWindowStateContainer positionShowState) {
        GameState = gameState;
        LoggerState = loggerState;
        PositionShowState = positionShowState;
    }

    public InnerWindowStateContainer PositionShowState;
}
