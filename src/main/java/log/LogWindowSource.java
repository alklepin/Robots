package log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Источник сообщений лога с ограничением размера.
 * Хранение записей делегировано в потокобезопасное кольцевое
 * хранилище {@link BoundedLogStorage}, благодаря чему количество
 * записей не превышает заданной ёмкости, а старые записи вытесняются.
 */
public class LogWindowSource
{
    private final BoundedLogStorage m_storage;
    private final ArrayList<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength)
    {
        m_storage = new BoundedLogStorage(iQueueLength);
        m_listeners = new ArrayList<LogChangeListener>();
    }

    public void registerListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener)
    {
        synchronized(m_listeners)
        {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage)
    {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        m_storage.append(entry);
        LogChangeListener [] activeListeners = m_activeListeners;
        if (activeListeners == null)
        {
            synchronized (m_listeners)
            {
                if (m_activeListeners == null)
                {
                    activeListeners = m_listeners.toArray(new LogChangeListener [0]);
                    m_activeListeners = activeListeners;
                }
            }
        }
        for (LogChangeListener listener : activeListeners)
        {
            listener.onLogChanged();
        }
    }

    public int size()
    {
        return m_storage.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count)
    {
        return m_storage.range(startFrom, count);
    }

    public Iterable<LogEntry> all()
    {
        return m_storage.snapshot();
    }
}
