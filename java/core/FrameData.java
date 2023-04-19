package core;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class FrameData implements Serializable {
    private final Dimension pos;
    private final Dimension size;
    private final boolean isMinimized;

    public FrameData(JInternalFrame internalFrame) {
        this.pos = new Dimension(internalFrame.getX(), internalFrame.getY());
        this.size = internalFrame.getSize();
        this.isMinimized = internalFrame.isIcon();
    }

    public Dimension getPos() {
        return pos;
    }

    public Dimension getSize() {
        return size;
    }

    public boolean isMinimized() {
        return isMinimized;
    }
}
