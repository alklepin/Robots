package serialization;

import javax.swing.*;
import java.awt.*;

public class FrameDescriber extends ComponentDescriber{
    private int extendedState;

    FrameDescriber(JFrame frame) {
        super(frame);
        extendedState = frame.getExtendedState();
    }

    void restoreState(JFrame frame) {
        super.restoreState(frame);
        frame.setExtendedState(extendedState);
    }
}
