package gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements Recoverable
{
    private final GameVisualizer m_visualizer;
    public GameWindow()
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public Map<String, DictState> saveState(JInternalFrame frame, Map<String, DictState> map) {
        FrameStates frameStates = new FrameStates();
        return frameStates.addStates(frame, "model", map);
    }

    @Override
    public void setRecoveryState(JInternalFrame frame, Map<String, DictState> map) {
        DictState dictState = map.get("model");
        frame.setSize(Integer.valueOf(dictState.getDictState().get("width")), Integer.valueOf(dictState.getDictState().get("height")));
        frame.setLocation(Integer.valueOf(dictState.getDictState().get("x")), Integer.valueOf(dictState.getDictState().get("y")));
        //setVisible(Boolean.valueOf(dictState.getDictState().get("is_extended")));
        if (Boolean.valueOf(dictState.getDictState().get("is_icon"))) {
            try {
                frame.setIcon(true);
            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        }
    }
}
