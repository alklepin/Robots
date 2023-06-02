package log;

import java.util.ArrayList;
import java.util.Collections;

public class LogWindowSource {
    /**
     * Что починить:
     * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
     * удерживаемыми в памяти)
     * 2. Этот класс хранит активные сообщения лога, но в такой реализации он
     * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено
     * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
     * ограниченного размера)
     */
    private int m_iQueueLength;

    private List m_msgs;
    private final ArrayList<LogChangeListener> m_listeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_msgs = new List(30);
        m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_msgs.put(entry);

        synchronized (m_listeners) {
            for (LogChangeListener listener : m_listeners) {
                listener.onLogChanged();
            }
        }
    }

    public int size() {
        return m_msgs.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= m_msgs.size()) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_msgs.size());
        return m_msgs.range(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return m_msgs.range(0, size());
    }
}
