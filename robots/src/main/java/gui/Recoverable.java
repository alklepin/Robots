package gui;

import javax.swing.*;
import java.util.Map;

public interface Recoverable {
    Map<String, DictState> saveState(JInternalFrame frame, Map<String, DictState> map);
    void setRecoveryState(JInternalFrame frame, Map<String, DictState> map);
}
