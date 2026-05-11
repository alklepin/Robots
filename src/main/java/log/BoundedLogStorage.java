package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Потокобезопасное хранилище записей лога ограниченного размера.
 * Реализовано как кольцевой буфер: при достижении предельной ёмкости
 * новая запись вытесняет самую старую. Все операции защищены одним
 * монитором, а методы чтения возвращают снимки (копии), благодаря
 * чему добавление записей во время итерации не ломает читателя.
 */
public class BoundedLogStorage
{
    private final LogEntry[] m_buffer;
    private final int m_capacity;
    private int m_head;
    private int m_size;

    public BoundedLogStorage(int capacity)
    {
        if (capacity <= 0)
        {
            throw new IllegalArgumentException("capacity must be positive");
        }
        m_capacity = capacity;
        m_buffer = new LogEntry[capacity];
        m_head = 0;
        m_size = 0;
    }

    public synchronized void append(LogEntry entry)
    {
        int writeIndex = (m_head + m_size) % m_capacity;
        m_buffer[writeIndex] = entry;
        if (m_size < m_capacity)
        {
            m_size++;
        }
        else
        {
            m_head = (m_head + 1) % m_capacity;
        }
    }

    public synchronized int size()
    {
        return m_size;
    }

    public synchronized int capacity()
    {
        return m_capacity;
    }

    public synchronized List<LogEntry> range(int startFrom, int count)
    {
        if (count <= 0 || startFrom < 0 || startFrom >= m_size)
        {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_size);
        List<LogEntry> result = new ArrayList<LogEntry>(indexTo - startFrom);
        for (int i = startFrom; i < indexTo; i++)
        {
            result.add(m_buffer[(m_head + i) % m_capacity]);
        }
        return result;
    }

    public synchronized List<LogEntry> snapshot()
    {
        List<LogEntry> result = new ArrayList<LogEntry>(m_size);
        for (int i = 0; i < m_size; i++)
        {
            result.add(m_buffer[(m_head + i) % m_capacity]);
        }
        return result;
    }
}
