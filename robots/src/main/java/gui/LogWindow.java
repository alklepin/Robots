package gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener, Recoverable
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

    public LogWindow() {}

    public LogWindow(LogWindowSource logSource)
    {
        super("Протокол работы", true, true, true, true);
        LogWindow logWindow = new LogWindow();
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public Map<String, DictState> saveState(JInternalFrame frame, Map<String, DictState> map) {
        FrameStates frameStates = new FrameStates();
        return frameStates.addStates(frame, "log", map);
    }

    @Override
    public void setRecoveryState(JInternalFrame frame, Map<String, DictState> map) {
        DictState dictState = map.get("log");
        frame.setSize(Integer.valueOf(dictState.getDictState().get("width")), Integer.valueOf(dictState.getDictState().get("height")));
        frame.setLocation(Integer.valueOf(dictState.getDictState().get("x")), Integer.valueOf(dictState.getDictState().get("y")));
        if (Boolean.valueOf(dictState.getDictState().get("is_icon"))) {
            try {
                frame.setIcon(true);

            } catch (PropertyVetoException e) {
                e.printStackTrace();
            }
        }
    }
}
