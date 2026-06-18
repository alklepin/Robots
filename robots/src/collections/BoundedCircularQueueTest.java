package collections;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class BoundedCircularQueueTest {

    @Test
    void testOfferAndPoll() {
        BoundedCircularQueue<String> q = new BoundedCircularQueue<>(3);
        assertTrue(q.offer("A"));
        assertTrue(q.offer("B"));
        assertTrue(q.offer("C"));
        assertEquals(3, q.size());
        assertEquals("A", q.poll());
        assertEquals("B", q.peek());
        assertEquals(2, q.size());
    }

    @Test
    void testOverflow() {
        BoundedCircularQueue<String> q = new BoundedCircularQueue<>(3);
        q.offer("A");
        q.offer("B");
        q.offer("C");
        q.offer("D"); // перезаписывает A
        assertEquals(3, q.size());
        assertEquals("B", q.poll());
        assertEquals("C", q.poll());
        assertEquals("D", q.poll());
        assertNull(q.poll());
    }

    @Test
    void testGetAll() {
        BoundedCircularQueue<Integer> q = new BoundedCircularQueue<>(5);
        for (int i = 0; i < 5; i++) q.offer(i);
        assertEquals(List.of(0, 1, 2, 3, 4), q.getAll());
    }

    @Test
    void testGetRange() {
        BoundedCircularQueue<String> q = new BoundedCircularQueue<>(5);
        q.offer("a");
        q.offer("b");
        q.offer("c");
        q.offer("d");
        assertEquals(List.of("b", "c"), q.getRange(1, 3));
    }

    @Test
    void testIteratorSnapshot() {
        BoundedCircularQueue<String> q = new BoundedCircularQueue<>(3);
        q.offer("X");
        q.offer("Y");
        Iterator<String> it = q.iterator();
        q.offer("Z"); // добавление не ломает итератор
        assertTrue(it.hasNext());
        assertEquals("X", it.next());
        assertEquals("Y", it.next());
        assertFalse(it.hasNext());
    }
}
