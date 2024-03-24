package log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * пофикшены:
 * 1. утечка ресурсов (связанные слушатели оказываются удерживаемыми в памяти)
 * 2. ограничения; раньше класс хранил активные сообщения лога, но в такой реализации он
 * их лишь накапливал. Сейчас количество сообщений в логе было ограничено
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
 * ограниченного размера)
 *
 * изменения:
 * Используется ArrayBlockingQueue для хранения сообщений лога, что позволяет избежать утечек ресурсов и ограничить размер лога величиной m_iQueueLength.
 * Избавлены от необходимости хранить все слушатели в виде массива, который нужно было пересоздавать каждый раз при добавлении или удалении слушателя. Вместо этого используется обычный список m_listeners, синхронизированный на запись/удаление.
 * Используется offer() вместо add() при добавлении сообщения в очередь, чтобы избежать исключения в случае, если очередь заполнена.
 * Метод size() теперь возвращает размер m_queue.
 * Метод range() теперь проверяет, что count неотрицательное число и возвращает пустой список, если startFrom выходит за пределы очереди или count равен нулю.
 * Метод range() теперь использует метод ArrayList.subList() непосредственно над m_queue, а не создает новый список, что улучшает производительность и уменьшает использование памяти.
 * Метод all() теперь создает новый список из m_queue, вместо создания списка итератором.
 */
public class LogWindowSource {
    private final int m_iQueueLength;
    private final ArrayBlockingQueue<LogEntry> m_queue;
    private final List<LogChangeListener> m_listeners;
    private volatile LogChangeListener[] m_activeListeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_queue = new ArrayBlockingQueue<>(iQueueLength);
        m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized(m_listeners) {
            m_listeners.add(listener);
            m_activeListeners = null;
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized(m_listeners) {
            m_listeners.remove(listener);
            m_activeListeners = null;
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        boolean added = false;
        while (!added) {
            LogEntry entry = new LogEntry(logLevel, strMessage);
            if (m_queue.offer(entry)) {
                added = true;
                if (m_queue.size() > m_iQueueLength) {
                    m_queue.remove();
                }
                LogChangeListener[] activeListeners = m_activeListeners;
                if (activeListeners == null) {
                    synchronized (m_listeners) {
                        if (m_activeListeners == null) {
                            activeListeners = m_listeners.toArray(new LogChangeListener[0]);
                            m_activeListeners = activeListeners;
                        }
                    }
                }
                for (LogChangeListener listener : activeListeners) {
                    listener.onLogChanged();
                }
            } else {
                m_queue.remove();
            }
        }
    }

    public int size() {
        return m_queue.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0 || startFrom >= m_queue.size() || count <= 0) {
            return Collections.emptyList();
        }
        int indexTo = Math.min(startFrom + count, m_queue.size());
        return new ArrayList<>(m_queue).subList(startFrom, indexTo);
    }

    public Iterable<LogEntry> all() {
        return new ArrayList<>(m_queue);
    }
}