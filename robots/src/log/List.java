package log;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.*;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 * 2. Этот класс хранит активные сообщения лога, но в такой реализации он
 * их лишь накапливает. Надо же, чтобы количество сообщений в логе было ограничено
 * величиной m_iQueueLength (т.е. реально нужна очередь сообщений
 * ограниченного размера)
 */
public class List {
    private final int size;
    public int curSize;

    public List(int size) {
        this.size = size;
        curSize = 0;
    }

    private static class Node {
        final LogEntry item;
        final AtomicReference<Node> next;

        Node(LogEntry item, Node next) {
            this.item = item;
            this.next = new AtomicReference<Node>(next);
        }
    }

    private AtomicReference<Node> tail = new AtomicReference<>();
    private AtomicReference<Node> head
            = new AtomicReference(new Node(null, null));

    public void put(LogEntry item) {
        Node newNode = new Node(item, null);
        var curHead = head.get();
        if (curHead.item == null || size == 1) {
            if (!head.compareAndSet(curHead, newNode)) {
                put(item);
                return;
            }
            curSize++;
            return;
        }
        if (tail.get() == null) {
            if (!tail.compareAndSet(null, newNode)) {
                put(item);
                return;
            }
            if (!head.get().next.compareAndSet(null, tail.get())) {
                put(item);
                return;
            }
            curSize++;
            return;
        }
        if (!tail.get().next.compareAndSet(null, newNode)) {
            put(item);
            return;
        }
        var previous = tail.get();
        if (!tail.compareAndSet(previous, previous.next.get())) {
            put(item);
            return;
        }
        curSize++;
        if (size < curSize) {
            var cur = head.get();
            if (!head.compareAndSet(cur, cur.next.get())) {
                put(item);
                return;
            }
            curSize--;
        }

    }

    public int size() {
        return curSize;
    }

    public Node[] getCurNodes() {
        var result = new Node[size];
        var curIndex = 0;
        var start = this.head;
        while (start.get() != null && start.get().item != null) {
            synchronized (start.get()) {
                var prev = start.get();
                result[curIndex] = prev;
                start = prev.next;
                curIndex++;
            }
        }

        return result;
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        var nodeList = Arrays.copyOfRange(getCurNodes(), startFrom, count);
        var result = new LogEntry[nodeList.length];
        var cur = 0;
        for (Node node : nodeList) {
            result[cur] = node.item;
            cur++;
        }
        return java.util.List.of(result);
    }

    public void print(Node[] arr) {
        for (Node node : arr) {
            if (node != null)
                out.println(node.item.getMessage());
        }
        System.out.println("*****");
    }

}
