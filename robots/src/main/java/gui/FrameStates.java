package gui;

import javax.swing.*;
import java.util.Map;

public class FrameStates {
    public Map<String, DictState> addStates(JInternalFrame frame, String windowName, Map<String, DictState> map) {
        DictState formDictState = new DictState();
        formDictState.addState("width", String.valueOf(frame.getWidth()));
        formDictState.addState("height", String.valueOf(frame.getHeight()));
        formDictState.addState("x", String.valueOf(frame.getX()));
        formDictState.addState("y", String.valueOf(frame.getY()));
        formDictState.addState("is_icon", String.valueOf(frame.isIcon()));
        map.put(windowName, formDictState);
        return map;
    }
}
