package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JInternalFrame.JDesktopIcon;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;

    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы", true, true, true, true);
        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        
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
    
    public String getPosition()
    {
		String position = String.format( "%d,%d,%d,%d,%b", getX(), getY(), 
									getWidth(), getHeight(), isIcon() );
		return position;
    }
    
    public void restorePosition(String position)
    {
    	// set coordinates and size
    	String pos[] = position.split(",");
		setBounds(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]),
					Integer.parseInt(pos[2]), Integer.parseInt(pos[3]));
		
		 // minimize if necessary
		if (Boolean.parseBoolean(pos[4]))
		{
			try {
				setIcon(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
    }
}
