package log;

import collections.BoundedCircularQueue;
import java.util.List;

public class LogBuffer {
    private final BoundedCircularQueue<LogEntry> buffer;

    public LogBuffer(int capacity) {
        buffer = new BoundedCircularQueue<>(capacity);
    }

    public void add(LogEntry entry) {
        buffer.offer(entry);
    }

    public int size() {
        return buffer.size();
    }

    public List<LogEntry> getAll() {
        return buffer.getAll();
    }

    public List<LogEntry> getRange(int start, int end) {
        return buffer.getRange(start, end);
    }

    public void clear() {
        buffer.clear();
    }
}
