package org.iffomko.notes;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;

/**
 * <p>Класс итератора для коллекции <code>Notes</code></p>
 * @param <Type> - тип значений, по которым итерируется итератор
 */
public class NotesIterator<Type> implements Iterator<Type> {
    private LinkedElement<Type> next;
    private LinkedElement<Type> current;

    private final Semaphore SEMAPHORE = new Semaphore(1, true);

    /**
     * <p>Создает и инициализирует объект итератор</p>
     * @param element - элемент, с которого начнется итерация по элементам коллекции
     */
    public NotesIterator(LinkedElement<Type> element) {
        this.next = element;
        this.current = null;
    }

    /**
     * <p>Возвращает следующий существующий элемент в коллекции</p>
     * @return - элемент коллекции
     */
    private LinkedElement<Type> findNext() {
        try {
            SEMAPHORE.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        LinkedElement<Type> item = this.next;

        while (!item.exists && item.next != null) {
            this.next = item = this.next.next;
        }

        SEMAPHORE.release();

        return item;
    }

    /**
     * Возвращает {@code true}, если итерация ещё содержит элементы. (Другими словами, возвращает {@code true}, если
     * {@link #next} вернет элемент, а не вызовет исключение.)
     *
     * @return {@code true} если итерация ещё содержит элементы
     */
    @Override
    public boolean hasNext() {
        return findNext().exists;
    }

    /**
     * <p>Возвращает следующий элемент в итерации</p>
     *
     * @return - следующий элемент в итерации
     * @throws NoSuchElementException - если итерация не содержит больше элементов
     */
    @Override
    public Type next() {
        LinkedElement<Type> next = findNext();

        if (!findNext().exists) {
            throw new NoSuchElementException();
        }

        this.current = next;
        this.next = next.next;

        return this.current.value;
    }

    /**
     * <p>Операция не поддерживается</p>
     * @throws UnsupportedOperationException - всегда возвращается следующая ошибка, так как операция не поддерживается
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}