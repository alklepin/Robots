package gui.serial;

import java.io.Serializable;

public class MainWindowStateContainer implements Serializable {
    public InnerWindowStateContainer m_gameState;
    public InnerWindowStateContainer m_loggerState;
    public InnerWindowStateContainer m_positionShowState;

    public MainWindowStateContainer(InnerWindowStateContainer gameState, InnerWindowStateContainer loggerState, InnerWindowStateContainer positionShowState) {
        m_gameState = gameState;
        m_loggerState = loggerState;
        m_positionShowState = positionShowState;
    }


}
