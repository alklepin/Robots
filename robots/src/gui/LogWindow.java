package gui;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.util.ResourceBundle;

public class LogWindow extends JInternalFrame implements LogChangeListener
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    private final ResourceBundle localization;

    public LogWindow(LogWindowSource logSource, ResourceBundle localization)
    {
        super(localization.getString("protocol"), true, true, true, true);
        this.localization = localization;
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Confirm close window
        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
                Object[] options = { localization.getString("yes"), localization.getString("no") };
                if (JOptionPane.showOptionDialog(e.getInternalFrame(),
                        localization.getString("closeWindowQuestion"), localization.getString("closeWindowTitle"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, null) == 0)
                {
                    m_logSource.unregisterListener((LogChangeListener) e.getInternalFrame());
                    e.getInternalFrame().getDesktopPane().getDesktopManager().closeFrame(e.getInternalFrame());
                }
            }
        });
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
}
