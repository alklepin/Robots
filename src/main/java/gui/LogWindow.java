package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import listeners.InternalFrameListenerImpl;
import localization.LocalizationManager;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;
import serialization.WindowStorage;

public class LogWindow extends JInternalFrame implements LogChangeListener {
    private TextArea m_logContent;

    public WindowStorage m_windowStorage;
    public LogWindowSource m_logSource;

    public LogWindow(LogWindowSource logSource, LocalizationManager localizationManager, WindowStorage windowStorage) {
        super(null, true, true, true, true);
        localizationManager.bindField("logWindow.title", this::setTitle);

        m_windowStorage = windowStorage;

        m_logSource = logSource;
        m_logSource.registerListener(this);

        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        getContentPane().add(m_logContent, BorderLayout.CENTER);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addInternalFrameListener(new InternalFrameListenerImpl(localizationManager, windowStorage, this));

//        setVisible(true);
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
    public void doDefaultCloseAction() {
        m_logSource.unregisterListener(this);
        super.doDefaultCloseAction();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }
}