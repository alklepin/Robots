package org.iffomko.notes;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <p>
 *     Структура данных для хранения записей. Этот объект является потока безопасным, то есть гарантируется целостность
 *     данных в многопоточном режиме.
 * </p>
 * <p>
 *     Эта структура данных имеет ограниченный размер и при добавлении новых элементов
 *     и заполненности объекта старые данные будут вытесняться.
 * </p>
 * <p>
 *     Добавление и чтение данных будут выполняться за константное время.
 *     Удаление последнего добавленный элемента происходит тоже за константное время.
 * </p>
 * <p>
 *     Этот класс является темпоральной структурой данных (т. е. возвращает потока безопасный итератор).
 * </p>
 */
public class Notes<Type> implements Iterable<Type> {
    private Map<Type, LinkedElement<Type>> items;
    private Map<Integer, Type> byIndex;
    private LinkedElement<Type> head;
    private LinkedElement<Type> placeholder;
    private int size = 10;
    private int count;
    private int start;
    private final Object synchronizedObject = new Object();

    /**
     * <p>Создает и инициализирует экземпляр коллекции</p>
     * @param size - размер коллекции
     */
    public Notes(int size) {
        if (size > 10)
            this.size = size;

        items = new HashMap<>();
        byIndex = new HashMap<>();
        placeholder = new LinkedElement<>();
        placeholder.index = 0;
        placeholder.exists = false;
        head = placeholder;
        start = 0;
    }

    /**
     * <p>Проверяет является ли коллекция пустой</p>
     * @return - <code>true</code>, если коллекция пустая, или <code>false</code> - в противном случае
     */
    public boolean isEmpty() {
        return head == placeholder;
    }

    /**
     * <p>Возвращает количество элементов в коллекции. Если она полная, то возвращается размер коллекции</p>
     * @return - количество элементов в коллекции
     */
    public int size() {
        return count;
    }

    /**
     * <p>Проверяет, содержится ли значение в коллекции</p>
     * @param o - значение, вхождение которого хотим проверить
     * @return - <code>true</code>, если содержится, или <code>false</code, если не содержится
     */
    public boolean containsValue(Object o) {
        if (o == null) {
            return false;
        }

        synchronized (synchronizedObject) {
            try {
                Type other = (Type) o;

                return items.containsKey(other);
            } catch (ClassCastException e) {
                // just ignore
            }
        }

        return false;
    }

    /**
     * <p>Добавляет элемент в коллекцию. Если коллекция полная, то вытесняется первый элемент в коллекции</p>
     * <p>Добавление происходит за константное время</p>
     * @param element - элемент, который хотим добавить
     * @return - <code>true</code>, если элемент был добавлен, или <code>false</code> - в противном случае
     */
    public boolean add(Type element) {
        synchronized (synchronizedObject) {
            try {
                if (size == count) {
                    items.remove(head.value);
                    byIndex.remove(head.index);
                    head.exists = false;
                    head.next.prev = null;
                    head = head.next;

                    start = head.index;
                    count--;
                }

                LinkedElement<Type> current = placeholder;
                current.value = element;
                current.exists = true;

                byIndex.put(current.index, element);
                items.put(element, current);

                current.next = new LinkedElement<>();

                placeholder = current.next;
                placeholder.prev = current;
                placeholder.exists = false;
                placeholder.index = current.index + 1;

                count++;

                return true;
            } catch (
                    UnsupportedOperationException |
                    ClassCastException |
                    NullPointerException |
                    IllegalArgumentException e
            ) {
                return false;
            }
        }
    }

    /**
     * <p>Возвращает список значений элементов коллекции с <code>beginIndex</code> до <code>endIndex</code></p>
     * @param beginIndex - начальный индекс сегмента
     * @param endIndex - конечный индекс сегмента
     * @throws IndexOutOfBoundsException - если <code>beginIndex</code> или <code>endIndex</code> вышли за границы
     * коллекции
     * @return - список значений сегмента
     */
    public List<Type> getSegment(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > (size() - 1)) {
            throw new IndexOutOfBoundsException();
        }

        List<Type> segment = new ArrayList<>();

        synchronized (synchronizedObject) {
            for (int i = start + beginIndex; i <= start + endIndex; i++) {
                segment.add(get(i));
            }
        }

        return segment;
    }

    /**
     * <p>
     *     Возвращает значение последнего добавленного элемента в коллекцию. Если элементов в коллекции нет, то будет
     *     возвращено <code>null</code>
     * </p>
     * @return - значение последнего элемента
     */
    public Type peek() {
        synchronized (synchronizedObject) {
            if (head == placeholder) {
                return null;
            }

            return placeholder.prev.value;
        }
    }

    /**
     * <p>Удаляет последний элемент в коллекции</p>
     * @return - возвращает удаленный элемент или <code>null</code>, если элементов в коллекции не было
     */
    public Type pop() {
        synchronized (synchronizedObject) {
            if (head == placeholder) {
                return null;
            }

            LinkedElement<Type> lastElement = placeholder.prev;

            Type returnValue = lastElement.value;

            items.remove(lastElement.value);
            byIndex.remove(lastElement.index);
            lastElement.exists = false;

            if (count > 1) {
                lastElement.prev.next = placeholder;
                placeholder.prev = lastElement.prev;
                lastElement.prev = null;

                count--;

                return returnValue;
            }

            count--;

            head = placeholder;
            head.prev = null;

            return returnValue;
        }
    }

    /**
     * <p>Возвращает значение элемента по индексу</p>
     * @param index - индекс, значение элемента которого ты хочешь получить
     * @throws IndexOutOfBoundsException - если ввели индекс, который выход за границы коллекции
     * @return - значение элемента
     */
    public Type get(int index) {
        synchronized (synchronizedObject) {
            Type value = byIndex.get(start + index);

            if (value == null) {
                throw new IndexOutOfBoundsException();
            }

            return value;
        }
    }

    /**
     * Возвращает итератор по элементам типа {@code Type}.
     *
     * @return - итератор
     */
    @Override
    public Iterator<Type> iterator() {
        return new NotesIterator<>(head);
    }

    /**
     * Выполняет данное действие для каждого элемента {@code Iterable} до тех пор, пока все элементы не будут обработаны
     * или действие не вызовет исключение. Действия выполняются в порядке итерации, если такой порядок указан.
     * Исключения, создаваемые действием, связаны с вызывающей стороной.
     * <p>
     *      Поведение этого метода не определено, если действие выполняет побочные эффекты, которые изменяют базовый
     *      источник элементов, если только переопределяющий класс не указал политику одновременного изменения.
     * </p>
     *
     * @param action - действие, которое необходимо выполнить для каждого элемента
     * @throws NullPointerException - если указанное действие равно null
     * @implSpec <p>Реализация по умолчанию ведет себя так, как если бы:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     * </p>
     * @since 1.8
     */
    @Override
    public void forEach(Consumer<? super Type> action) {
        if (action == null) {
            throw new NullPointerException();
        }

        for (Type current : this) {
            action.accept(current);
        }
    }

    /**
     * <p>Возвращает строковое представление всех элементов коллекции</p>
     * @return - строковое представление всех элементов коллекции
     */
    @Override
    public final String toString() {
        StringBuilder view = new StringBuilder();

        view.append("[");

        for (Type current : this) {
            view.append(current.toString());

            if (size() > 1 && (items.get(current).index < (start + size() - 1))) {
                view.append(", ");
            }
        }

        view.append("]");

        return view.toString();
    }
}
