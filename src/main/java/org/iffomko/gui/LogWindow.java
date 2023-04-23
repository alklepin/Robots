package org.iffomko.gui;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.iffomko.log.LogChangeListener;
import org.iffomko.log.LogEntry;
import org.iffomko.log.LogWindowSource;
import org.iffomko.savers.Savable;

/**
 * Окно с содержанием логов
 */
public class LogWindow extends JInternalFrame implements LogChangeListener, Savable
{
    private LogWindowSource m_logSource;
    private TextArea m_logContent;
    private final String prefix;

    /**
     * Создает окно с логами
     * @param logSource - объект со всеми логами
     */
    public LogWindow(LogWindowSource logSource) 
    {
        super("Протокол работы", true, true, true, true);

        ResourceBundle resourceBundle = ResourceBundle.getBundle(
                "org.iffomko.gui.localizationProperties.logWindow.LogWindowResource",
                new Locale(System.getProperty("user.language"), System.getProperty("user.country"))
        );

        this.setTitle(resourceBundle.getString("logWindowTitle"));

        m_logSource = logSource;
        m_logSource.registerListener(this);
        m_logContent = new TextArea("");
        m_logContent.setSize(200, 500);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();

        prefix = "log";
    }

    /**
     * Перебирает все накопленные логи и добавляет их текст в область с текстом
     */
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

    /**
     * Этот метод дергается при изменении логов
     */
    @Override
    public void onLogChanged()
    {
        EventQueue.invokeLater(this::updateLogContent);
    }

    /**
     * <p>Возвращает состояние настроек класса, который нужно сохранить</p>
     *
     * @return - любой объект, который реализует интерфейс <code>Map</code>
     */
    @Override
    public Map<String, String> save() {
        Map<String, String> state = new HashMap<>();

        if (state.get("isIconified") == null) {
            state.put("isIconified", String.valueOf(isIcon()));
        } else {
            state.replace("isIconified", String.valueOf(isIcon()));
        }

        double x = getLocation().getX();
        double y = getLocation().getY();
        int width = getWidth();
        int height = getHeight();

        if (state.get("x") == null) {
            state.put("x", String.valueOf(x));
        } else {
            state.replace("x", String.valueOf(x));
        }

        if (state.get("y") == null) {
            state.put("y", String.valueOf(y));
        } else {
            state.replace("y", String.valueOf(y));
        }

        if (state.get("width") == null) {
            state.put("width", String.valueOf(width));
        } else {
            state.replace("width", String.valueOf(width));
        }

        if (state.get("height") == null) {
            state.put("height", String.valueOf(height));
        } else {
            state.replace("height", String.valueOf(height));
        }

        return state;
    }

    /**
     * <p>Восстанавливает состояние объекта исходя из состояния, которое пришло ему извне</p>
     *
     * @param state - состояние, которое нужно восстановить
     */
    @Override
    public void restore(Map<String, String> state) {
        try {
            if (state == null) {
                return;
            }

            int width = Integer.parseInt(state.get("width"));
            int height = Integer.parseInt(state.get("height"));
            double x = Double.parseDouble(state.get("x"));
            double y = Double.parseDouble(state.get("y"));
            boolean isIconified = Boolean.parseBoolean(state.get("isIconified"));

            setSize(new Dimension(width, height));

            Point gameLocation = new Point();
            gameLocation.setLocation(x, y);

            setLocation(gameLocation);
            setIcon(isIconified);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Возвращает префикс, который ассоциируется с этим классом</p>
     *
     * @return - префикс
     */
    @Override
    public String getPrefix() {
        return prefix;
    }
}
