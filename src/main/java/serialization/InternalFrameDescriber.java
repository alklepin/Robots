package serialization;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.Serializable;

public class InternalFrameDescriber extends ComponentDescriber implements Serializable {
    private boolean isIcon;

    InternalFrameDescriber(JInternalFrame internalFrame) {
        super(internalFrame);
        isIcon = internalFrame.isIcon() || internalFrame.isClosed();
    }

    void restoreState(JInternalFrame internalFrame) {
        super.restoreState(internalFrame);
        try {
            internalFrame.setIcon(isIcon);
        } catch (PropertyVetoException exception) {
            exception.printStackTrace();
        }
    }
}
