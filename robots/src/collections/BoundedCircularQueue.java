package collections;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BoundedCircularQueue<E> extends AbstractQueue<E> {
    private final int capacity;
    private final Object[] buffer;
    private final AtomicInteger head = new AtomicInteger(0);
    private final AtomicInteger size = new AtomicInteger(0);

    public BoundedCircularQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacity must be positive");
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    @Override
    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        while (true) {
            int currentSize = size.get();
            if (currentSize < capacity) {
                if (size.compareAndSet(currentSize, currentSize + 1)) {
                    int pos = (head.get() + currentSize) % capacity;
                    buffer[pos] = e;
                    return true;
                }
            } else {
                int oldHead = head.get();
                int newHead = (oldHead + 1) % capacity;
                if (head.compareAndSet(oldHead, newHead)) {
                    buffer[oldHead] = e;
                    return true;
                }
            }
        }
    }

    @Override
    public E poll() {
        while (true) {
            int currentSize = size.get();
            if (currentSize == 0) return null;
            if (size.compareAndSet(currentSize, currentSize - 1)) {
                int idx = head.getAndUpdate(h -> (h + 1) % capacity);
                @SuppressWarnings("unchecked")
                E result = (E) buffer[idx];
                buffer[idx] = null;
                return result;
            }
        }
    }

    @Override
    public E peek() {
        int currentSize = size.get();
        if (currentSize == 0) return null;
        @SuppressWarnings("unchecked")
        E result = (E) buffer[head.get()];
        return result;
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public Iterator<E> iterator() {
        return snapshot().iterator();
    }

    public List<E> getAll() {
        return snapshot();
    }

    public List<E> getRange(int start, int end) {
        int currentSize = size.get();
        if (start < 0 || end > currentSize || start > end)
            throw new IndexOutOfBoundsException("Invalid range: " + start + ".." + end);
        int currentHead = head.get();
        List<E> result = new ArrayList<>(end - start);
        for (int i = start; i < end; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) buffer[(currentHead + i) % capacity];
            result.add(element);
        }
        return Collections.unmodifiableList(result);
    }

    private List<E> snapshot() {
        int currentHead = head.get();
        int currentSize = size.get();
        List<E> result = new ArrayList<>(currentSize);
        for (int i = 0; i < currentSize; i++) {
            @SuppressWarnings("unchecked")
            E element = (E) buffer[(currentHead + i) % capacity];
            result.add(element);
        }
        return Collections.unmodifiableList(result);
    }
}
