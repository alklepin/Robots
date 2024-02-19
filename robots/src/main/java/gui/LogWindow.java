package main.java.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.TextArea;
import java.util.Iterator;
import java.util.ResourceBundle;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import main.java.log.LogChangeListener;
import main.java.log.LogEntry;
import main.java.log.LogWindowSource;

public class LogWindow extends JInternalFrame implements LogChangeListener, Translatable {
    private LogWindowSource mLogSource;
    private TextArea mLogContent;

    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        this.mLogSource = logSource;
        this.mLogSource.registerListener(this);
        this.mLogContent = new TextArea("");
        this.mLogContent.setSize(200, 500);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.mLogContent, "Center");
        this.getContentPane().add(panel);
        this.pack();
        this.updateLogContent();
    }

    private void updateLogContent() {
        StringBuilder content = new StringBuilder();
        Iterator var2 = this.mLogSource.all().iterator();

        while(var2.hasNext()) {
            LogEntry entry = (LogEntry)var2.next();
            content.append(entry.getMessage()).append("\n");
        }

        this.mLogContent.setText(content.toString());
        this.mLogContent.invalidate();
    }

    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    public void setTranslate(ResourceBundle bundle) {
        this.setTitle(bundle.getString("logWindowHeader"));
    }
}
