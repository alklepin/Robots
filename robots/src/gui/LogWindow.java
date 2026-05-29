package gui;

import localization.LocalizationManager;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import log.Logger;

import javax.swing.*;
import java.awt.*;

public class LogWindow extends JInternalFrame implements LogChangeListener {
    private final LogWindowSource logSource;
    private final TextArea logContent;
    private final LocalizationManager loc = LocalizationManager.getInstance();

    public LogWindow(LogWindowSource logSource) {
        super(LocalizationManager.getInstance().getString("window.log.title"), true, true, true, true);
        this.logSource = logSource;
        this.logSource.registerListener(this);
        logContent = new TextArea("");
        logContent.setEditable(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
        Logger.debug(loc.getString("log.start"));
        loc.addListener(this::refreshLanguage);
    }

    private void refreshLanguage() {
        setTitle(loc.getString("window.log.title"));
        logSource.clear();
        Logger.debug(loc.getString("log.start"));
    }

    private void updateLogContent() {
        StringBuilder sb = new StringBuilder();
        for (LogEntry entry : logSource.all()) {
            sb.append(entry.getMessage()).append("\n");
        }
        logContent.setText(sb.toString());
    }

    @Override
    public void onLogChanged() {
        SwingUtilities.invokeLater(this::updateLogContent);
    }
}
