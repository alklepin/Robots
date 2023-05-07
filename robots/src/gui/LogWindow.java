package gui;

import java.awt.*;
import java.io.*;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener, IObjectState
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

    protected void save(String filename){
        File file = new File(filename);
        try (OutputStream os = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
            oos.writeObject(getName());
            oos.writeObject(getLocation().x);
            oos.writeObject(getLocation().y);
            oos.writeObject(getHeight());
            oos.writeObject(getWidth());
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    protected void load(String filename){
        try {
            InputStream is = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
            try {
                setName((String) ois.readObject());
                setLocation((Integer) ois.readObject(), (Integer) ois.readObject());
                setSize((Integer) ois.readObject(), (Integer) ois.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
