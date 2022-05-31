package gui;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class WindowStateDict {
    private Map<String, DictState> map;

    public WindowStateDict() {
        map = new HashMap<>();
    }

    public Map<String, DictState> getWindowStateDict() {
        return map;
    }

    public void setNewState(JInternalFrame[] frames) {
        GameWindow gameWindow = new GameWindow();
        LogWindow logWindow = new LogWindow();
        map = new HashMap<>();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].getTitle() == "Игровое поле") {
                map = gameWindow.saveState(frames[i], map);
            }
            else {
                map = logWindow.saveState(frames[i], map);
            }
        }
    }

    public void recoverNewState(JInternalFrame[] frames) {
        FileManager fileManager = new FileManager("read");
        Map<String, DictState> mapStates = fileManager.read();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].getTitle() == "Игровое поле") {
                GameWindow gameWindow = new GameWindow();
                gameWindow.setRecoveryState(frames[i], mapStates);
            }
            else {
                LogWindow logWindow = new LogWindow();
                logWindow.setRecoveryState(frames[i], mapStates);
            }
        }
    }
}
