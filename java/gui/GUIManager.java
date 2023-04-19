package gui;

import core.FrameData;
import core.UserDataSaver;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

public class GUIManager {
    private static final List<JInternalFrame> internalFrames = new ArrayList<>();
    public static JInternalFrame create(JInternalFrame internalFrame) {
        if (internalFrame instanceof GameWindow gameWindow) {
            FrameData frameData = (FrameData) UserDataSaver.getUserDataSaver().loadObject("game_window");
            setData(gameWindow, frameData);
            internalFrames.add(gameWindow);
            return gameWindow;
        }

        if (internalFrame instanceof LogWindow logWindow) {
            FrameData frameData = (FrameData) UserDataSaver.getUserDataSaver().loadObject("log_window");
            setData(logWindow, frameData);
            internalFrames.add(logWindow);
            return logWindow;
        }

        throw new RuntimeException("Can't create gui. " + internalFrame);
    }

    public static void removeAll() {
        for (JInternalFrame internalFrame : internalFrames) {
            remove(internalFrame);
        }
    }

    public static void remove(JInternalFrame internalFrame) {
        FrameData frameData = new FrameData(internalFrame);

        if (internalFrame instanceof GameWindow) {
            UserDataSaver.getUserDataSaver().saveObject("game_window", frameData);
            return;
        }

        if (internalFrame instanceof LogWindow) {
            UserDataSaver.getUserDataSaver().saveObject("log_window", frameData);
        }
    }

    private static void setData(JInternalFrame internalFrame, FrameData frameData) {
        if (frameData == null)
            return;

        internalFrame.setBounds(frameData.getPos().width, frameData.getPos().height,
                                frameData.getSize().width, frameData.getSize().height);

        if (internalFrame.isIconifiable()) {
            try {
                internalFrame.setIcon(frameData.isMinimized());
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
