package ru.kemichi.robots.gui.windows;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ru.kemichi.robots.log.LogChangeListener;
import ru.kemichi.robots.log.LogEntry;
import ru.kemichi.robots.log.LogWindowSource;
import ru.kemichi.robots.log.Logger;

public class LogWindow extends AbstractWindow implements LogChangeListener {
    private LogWindowSource logSource;
    private TextArea logContent;
    private ResourceBundle bundle;

    public LogWindow(LogWindowSource logSource, ResourceBundle bundle) {
        super(
                null,
                bundle.getString("logWindowHeader"),
                true,
                true,
                true,
                true
        );
        this.bundle = bundle;
        this.logSource = logSource;
        this.logSource.registerListener(this);
        logContent = new TextArea();
        logContent.setSize(200, 500);

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

    @Override
    public void defaultWindowSetup() {
        this.setLocation(10, 10);
        setMinimumSize(this.getSize());
        this.pack();
        this.setSize(300, 800);
        Logger.debug(bundle.getString("protocolOK"));
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }
}
