package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import static gui.Constants.LogWindowConstants.*;

public class LogWindow extends JInternalFrame implements LogChangeListener {
    private final LogWindowSource logSource;
    private final TextArea logContent;

    public LogWindow(LogWindowSource logSource) {
        super(INITIAL_LOG_MESSAGE, true, true, true, true);
        this.logSource = logSource;
        this.logSource.registerListener(this);
        logContent = new TextArea("");
        logContent.setSize(LOG_TEXT_AREA_WIDTH, LOG_TEXT_AREA_HEIGHT);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        logContent.setText(content.toString());
        logContent.invalidate();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}
