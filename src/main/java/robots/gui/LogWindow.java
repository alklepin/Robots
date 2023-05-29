package robots.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import robots.domain.InternalWindowJsonConfigurable;
import robots.log.LogChangeListener;
import robots.log.LogEntry;
import robots.log.LogWindowSource;
import robots.log.Logger;

public class LogWindow extends InternalWindowJsonConfigurable implements LogChangeListener
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean isUpdating = false;


    private int lastLogTime = 0;

    public LogWindow(LogWindowSource logSource)
    {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setDoubleBuffered(true);
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();

        executor.scheduleAtFixedRate(this::updateLogContentIfNeeded, 1, 1000, TimeUnit.MILLISECONDS);
    }
    private void updateLogContentIfNeeded() {
        if (isUpdating) {
            updateLogContent();
            isUpdating = false;
        }
    }


    public void load()
    {
        this.setLocation(10, 10);
        this.setSize(300, 800);
        setMinimumSize(this.getSize());
        this.pack();
        Logger.debug("Протокол работает");
    }

    public String getSavePath() {
        return "logWindow/config.json";
    }

    private void updateLogContent()
    {
        StringBuilder content = new StringBuilder();
        for (LogEntry entry : m_logSource.all())
        {
            content.append(entry.getMessage()).append("\n");
        }
//        m_logContent.append(content.toString());
        m_logContent.setText(content.toString());
        m_logContent.invalidate();
    }

    @Override
    public void onLogChanged()
    {
        isUpdating = true;
    }
}
