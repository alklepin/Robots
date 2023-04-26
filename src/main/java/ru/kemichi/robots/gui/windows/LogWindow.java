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
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
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
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea();
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.getMessage()).append("\n");
        }
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
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
