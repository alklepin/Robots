package org.iffomko.notes;

/**
 * <p>
 *     Это какой-то элемент внутри коллекции <code>Notes</code>, который хранит в себе: значение конкретного элемента,
 *     существует ли этот элемент в коллекции ещё или его уже удалили, его индекс и указатель на следующий и
 *     предыдущий элемент в коллекции
 * </p>
 * @param <Type> - тип значения для элемента в коллекции
 */
public class LinkedElement<Type> {
    volatile Type value;
    volatile boolean exists;

    volatile int index;

    volatile LinkedElement<Type> prev;
    volatile LinkedElement<Type> next;
}
