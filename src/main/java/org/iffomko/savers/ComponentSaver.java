package org.iffomko.savers;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <p>Под-словарь или по-другому класс, который сохраняет в себе состояние какой-то отдельно взятой компоненты</p>
 */
public class ComponentSaver extends AbstractMap<String, String> {
    private final Map<String, String> state;
    private final String prefix;

    /**
     * <p>Конструктор, который создает состояние для нашей компоненты и инициализирует префикс</p>
     * <p>Вылетит ошибка, если префикс равен <code>null</code> или пустой</p>
     * @param prefix - префикс, который ассоциируется состоянием компоненты. Во многих случаях это просто сокращенное
     *               название компоненты.
     */
    public ComponentSaver(String prefix) {
        this.state = new HashMap<>();

        if (prefix == null || prefix.equals("")) {
            throw new SaversException(SaversException.ILLEGAL_PREFIX_EXCEPTION_CODE);
        }

        this.prefix = prefix;
    }

    /**
     * <p>Возвращает множество объектов Entry, которые хранят в себе ключи и значение.</p>
     * <p><b>Ко всем ключам приписывается префикс</b></p>
     * @return - множество Entry<String, String> с ключами и их значениями
     */
    @Override
    public Set<Entry<String, String>> entrySet() {
        Set<Entry<String, String>> set = new HashSet<Entry<String, String>>();

        for (Entry<String, String> item : state.entrySet()) {
            Entry<String, String> entryItem;

            try {
                entryItem = new SimpleEntry<String, String>(
                        prefix + '.' + item.getKey(),
                        item.getValue()
                );
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new SaversException(SaversException.ILLEGAL_STATE_EXCEPTION_CODE);
            }

            set.add(entryItem);
        }

        return set;
    }

    /**
     * <p>Возвращает значение по ключу если оно не равно <code>null</code>. Иначе возвратит стандартное значение</p>
     * @param key - ключ, с которым ассоциировано значение
     * @param defaultValue - стандартное значение
     * @return - значение, которое привязано к ключу
     */
    @Override
    public String getOrDefault(Object key, String defaultValue) {
        String value;

        return ((value = get(key)) != null ? value : defaultValue);
    }

    /**
     * <p>
     *     Делает перебор по всем ключам и их значениям в порядке итерации метода <code>entrySet()</code>
     *     пока не закончится перебор или выбросится исключение. Все ключи и их значения передаются вызывающей стороне.
     * </p>
     * @param action действие, которое выполняется для каждой записи
     */
    @Override
    public void forEach(BiConsumer<? super String, ? super String> action) {
        Objects.requireNonNull(action);

        for (Map.Entry<String, String> item : entrySet()) {
            String key;
            String value;

            try {
                key = item.getKey();
                value = item.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new SaversException(SaversException.ILLEGAL_STATE_EXCEPTION_CODE);
            }

            action.accept(key, value);
        }
    }

    /**
     * <p>
     *     Все значения будут изменены для каждой записи результатом работы обработчика переданного в параметрах
     *     до тех пор пока не будут перебраны все записи или не будет выброшено исключение со стороны обработчика
     * </p>
     * @param function - функция, которая вызывается при каждой итерации
     */
    @Override
    public void replaceAll(BiFunction<? super String, ? super String, ? extends String> function) {
        Objects.requireNonNull(function);

        for (Entry<String, String> entry : entrySet()) {
            String key;
            String value;

            try {
                key = entry.getKey();
                value = entry.getValue();
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new SaversException(SaversException.ILLEGAL_STATE_EXCEPTION_CODE);
            }

            value = function.apply(key, value);

            try {
                entry.setValue(value);
            } catch (IllegalStateException ise) {
                // this usually means the entry is no longer in the map.
                throw new SaversException(SaversException.ILLEGAL_STATE_EXCEPTION_CODE);
            }
        }
    }

    /**
     * <p>Кладет значение в словарь если там значение пустое (равно <code>null</code>)</p>
     * @param key - ключ, по которому надо добавить значение в словарь
     * @param value - значение, которое мы хотим присвоить ключу в словаре
     * @return - предыдущее значение
     */
    @Override
    public String putIfAbsent(String key, String value) {
        String previousValue = get(key);

        if (previousValue == null) {
            put(key, value);
        }

        return previousValue;
    }

    /**
     * <p>Удаляет запись если совпавшему ключу и значению. Если ключ или значение не совпадает, то запись не будет удалена</p>
     * <p><b>Можно вводить ключи как с префиксом нашего состояния, так без</b></p>
     * @param key ключ, по которому надо удалить запись
     * @param value значение, по которому надо удалить запись
     * @return - результат удаления: <code>true</code> или <code>false</code>
     */
    @Override
    public boolean remove(Object key, Object value) {
        String stateKey = (String) key;

        if (stateKey.startsWith(prefix)) {
            stateKey = stateKey.substring(prefix.length() + 1);
        }

        return state.remove(stateKey, value);
    }

    /**
     * <p>Заменяет значение по ключу только если оно в момент замены совпадает с <code>oldValue</code></p>
     * <p><b>Можно вводить ключи как с префиксом нашего состояния, так без</b></p>
     * @param key - ключ, по которому надо заменить значение
     * @param oldValue - значение, с которым должно совпасть в момент замены
     * @param newValue - новое значение для ключа
     * @return - результат замены: <code>true</code> или <code>false</code>
     */
    @Override
    public boolean replace(String key, String oldValue, String newValue) {
        if (key.startsWith(prefix)) {
            key = key.substring(prefix.length() + 1);
        }

        return state.replace(key, oldValue, newValue);
    }

    /**
     * <p>Заменяет значение записи по ключу только в том случае, когда оно сопоставлено с каким-то значением</p>
     * <p><b>Можно вводить ключи как с префиксом нашего состояния, так без</b></p>
     * @param key - ключ, по которому надо заменить значение
     * @param value - значение, которое хотим заменить у ключа
     * @return - предыдущее значение или null
     */
    @Override
    public String replace(String key, String value) {
        if (key.startsWith(prefix)) {
            key = key.substring(prefix.length() + 1);
        }

        return state.replace(key, value);
    }

    /**
     * <p>
     *     Если указанный ключ еще не связан со значением (или сопоставлен нулю), пытается вычислить его значение с помощью заданной функции сопоставления и вводит его в эту карту, если значение не равно null.
     *     Если функция сопоставления возвращает значение <code>null</code>, отображение не записывается. Если функция сопоставления сама выдает (непроверенное) исключение, то исключение генерируется, и сопоставление не записывается.
     *     Наиболее распространенным использованием является создание нового объекта, служащего исходным отображенным значением или сохраненным в памяти результатом
     * </p>
     * <p><b>Можно вводить ключи как с префиксом нашего состояния, так без</b></p>
     * @param key - ключ, с которым должно быть связано указанное значение
     * @param mappingFunction функция отображения для вычисления значения
     * @return - текущее (существующее или вычисленное) значение, связанное с указанным ключом, или null, если вычисленное значение равно null
     */
    @Override
    public String computeIfAbsent(String key, Function<? super String, ? extends String> mappingFunction) {
        if (key.startsWith(prefix)) {
            key = key.substring(prefix.length() + 1);
        }

        return state.computeIfAbsent(key, mappingFunction);
    }

    /**
     * <p>
     *      Если значение для указанного ключа присутствует и не равно <code>null</code>, выполняется попытка вычислить новое сопоставление с учетом ключа и его текущего сопоставленного значения.
     *      Если функция переназначения возвращает значение <code>null</code>, сопоставление удаляется. Если функция переназначения сама выдает (непроверенное) исключение, исключение повторно генерируется, а текущее сопоставление остается неизменным
     * </p>
     * <p><b>Можно вводить ключи как с префиксом нашего состояния, так без</b></p>
     * @param key - ключ, с которым должно быть связано указанное значение
     * @param remappingFunction - функция переназначения для вычисления значения
     * @return - новое значение, связанное с указанным ключом, или <code>null</code>, если его нет
     */
    @Override
    public String computeIfPresent(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        if (key.startsWith(prefix)) {
            key = key.substring(prefix.length() + 1);
        }

        return state.computeIfPresent(key, remappingFunction);
    }

    /**
     * <p>
     *     Пытается вычислить сопоставление для указанного ключа и его текущего сопоставленного значения
     *     (или <code>null</code>, если текущее сопоставление отсутствует). Например, чтобы либо создать,
     *     либо добавить строку msg к сопоставлению значений:
     * </p>
     * <p>
     *     <code>
     *          map.compute(key, (k, v) -> (v == null) ? msg : v.concat(msg))
     *     </code>
     * </p>
     * <p>
     *     (Метод <code>merge()</code> часто проще использовать для таких целей.)
     *     Если функция переназначения возвращает значение <code>null</code>, сопоставление удаляется
     *     (или остается отсутствующим, если изначально отсутствовало).
     *     Если функция переназначения сама выдает (непроверенное) исключение,
     *     исключение повторно генерируется, а текущее сопоставление остается неизменным.
     *     Функция переназначения не должна изменять эту карту во время вычисления.
     * </p>
     * <p><b>Можно вводить ключи как с префиксом нашего состояния, так без</b></p>
     * @param key - ключ, с которым должно быть связано указанное значение
     * @param remappingFunction - функция переназначения для вычисления значения
     * @return - новое значение, связанное с указанным ключом, или <code>null</code>, если его нет
     */
    @Override
    public String compute(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        if (key.startsWith(prefix)) {
            key = key.substring(prefix.length() + 1);
        }

        return state.compute(key, remappingFunction);
    }

    /**
     * <p>
     *     Если указанный ключ еще не связан со значением или связан с <code>null</code>, свяжите его с заданным ненулевым значением.
     *     В противном случае заменяет связанное значение результатами данной функции переназначения или удаляет,
     *     если результат равен <code>null</code>. Этот метод может быть полезен при объединении нескольких
     *     сопоставленных значений для ключа.
     * </p>
     * <p>
     *     Например, чтобы либо создать, либо добавить строку <code>msg</code> к сопоставлению значений:
     * </p>
     * <p>
     *     <code>
     *         map.merge(key, msg, String::concat)
     *     </code>
     * </p>
     * <p>
     *     Если функция переназначения возвращает значение null, сопоставление удаляется.
     * </p>
     * <p>
     *     Если функция переназначения сама выдает (непроверенное) исключение,
     *     исключение повторно генерируется, а текущее сопоставление остается неизменным.
     * </p>
     * <p>
     *     Функция переназначения не должна изменять эту карту во время вычисления.
     * </p>
     * <p><b>Можно вводить ключи как с префиксом нашего состояния, так без</b></p>
     * @param key ключ, с которым должно быть связано результирующее значение
     * @param value - ненулевое значение, которое будет объединено с существующим значением
     *              связанный с ключом или, если не существует значения или значение null
     *              связан с ключом, должен быть связан с ключом
     * @param remappingFunction - функция переназначения для повторного вычисления значения, если оно присутствует
     * @return - новое значение, связанное с указанным ключом, или null, если с ключом не связано ни одно значение
     */
    @Override
    public String merge(String key, String value, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        if (key.startsWith(prefix)) {
            key = key.substring(prefix.length() + 1);
        }

        return state.merge(key, value, remappingFunction);
    }

    /**
     * <p>
     *     Возвращает представления в виде <code>Collection</code> для нашего состояния.
     *     <code>Collection</code> поддерживается состоянием, поэтому изменения, внесенные в состояние, отражаются в коллекции, и наоборот.
     *     Если состояние изменяется во время выполнения итерации по коллекции
     *     (за исключением собственной операции удаления итератора), результаты итерации не определены.
     *     <code>Collection</code> поддерживает удаление элемента, которое удаляет соответствующее отображение с состояния,
     *     с помощью операций <code>Iterator.remove</code>, <code>Collection.remove</code>,
     *     <code>removeAll</code>, <code>retainAll</code> и <code>clear</code>.
     *     Он не поддерживает операции добавления или <code>clear</code>.
     * </p>
     * @return - представление коллекции значений, содержащихся на этом состоянии
     */
    @Override
    public Collection<String> values() {
        return state.values();
    }

    /**
     * <p>
     *     Возвращает заданный вид ключей, содержащихся в нашем состоянии.
     *     Набор поддерживается состоянием, поэтому изменения в состоянии отражаются в наборе, и наоборот.
     *     Если состоянии изменяется во время выполнения итерации по набору
     *     (за исключением собственной операции удаления итератора),
     *     результаты итерации не определены.
     *     Набор поддерживает удаление элемента, которое удаляет соответствующее отображение у состояния,
     *     с помощью операций <code>Iterator.remove</code>, <code>Set.remove</code>, <code>removeAll</code>,
     *     <code>retainAll</code> и <code>clear</code>. Он не поддерживает операции добавления или <code>addAll</code>.
     * </p>
     * @return
     */
    @Override
    public Set<String> keySet() {
        Set<String> keys = new HashSet<String>();

        for (String key : state.keySet()) {
            keys.add(prefix + '.' + key);
        }

        return keys;
    }

    /**
     * <p>Возвращает значение по ключу</p>
     * @param key - ключ, с которым ассоциировано значение
     * @return - значение, которое ассоциировано с ключом
     */
    @Override
    public String get(Object key) {
        String stateKey = (String) key;

        if (!stateKey.startsWith(prefix)) {
            return state.get(stateKey);
        }

        return state.get(stateKey.substring(prefix.length() + 1));
    }

    /**
     * <p>Присваивает ключу значение</p>
     * @param key- ключ, с которым ассоциировано значение
     * @param value - значение, которое ассоциировано с ключом
     * @return - предыдущее значение, которое там хранилось
     */
    @Override
    public String put(String key, String value) {
        if (!key.startsWith(prefix)) {
            return state.put(key, value);
        }

        return state.put(key.substring(prefix.length() + 1), value);
    }
}
