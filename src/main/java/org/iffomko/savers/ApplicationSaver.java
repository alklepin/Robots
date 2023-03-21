package org.iffomko.savers;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Сохраняет состояние всех своих под-словарей в файлы
 */
public class ApplicationSaver extends AbstractMap<String, String> implements Saver {
    private static ApplicationSaver applicationSaver = null;

    private final Map<String, String> state;
    private boolean restored;

    /**
     * <p>Собирает и возвращает множество всех объектов, которые содержат ключи с определенным префиксом и их значение</p>
     * @param prefix - префикс, по которому будут фильтроваться ключи
     * @return - множество всех Entry<String, String>
     */
    private Set<Entry<String, String>> entrySetByPrefix(String prefix) {
        Set<Entry<String, String>> response = new HashSet<Entry<String, String>>();

        for (Entry<String, String> entry : state.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                response.add(entry);
            }
        }

        return response;
    }

    /**
     * <p>Возвращает префикс у ключа, если он есть. Если его нет, то возвращает <code>null</code></p>
     * @param key - ключ, у которого мы хотим получить префикс
     * @return - префикс
     */
    private String getPrefix(String key) {
        StringBuilder prefix = new StringBuilder();

        boolean prefixExist = false;

        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == '.') {
                prefixExist = true;
                break;
            }

            prefix.append(key.charAt(i));
        }

        if (!prefixExist || prefix.length() == 0) {
            return null;
        }

        return prefix.toString();
    }

    /**
     * <p>Пишет объект в бинарный файл</p>
     * @param file - бинарный файл, в который надо записать объект. Файл должен иметь корректный путь и существовать.
     *               В противном случае вылетит ошибка, и программа закончит свою работу досрочно.
     * @param object - объект, который надо записать в бинарный файл
     */
    private void writeObject(File file, Object object) {
        try {
            OutputStream output = new FileOutputStream(file);

            try {
                ObjectOutputStream objectOutput = new ObjectOutputStream(output);

                try {
                    objectOutput.writeObject(object);
                    objectOutput.flush();
                } finally {
                    objectOutput.close();
                }
            } finally {
                output.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Считывает объект с бинарного файла и возвращает его</p>
     * @param file - бинарный файл, с которого надо считать объект. Файл должен иметь корректный путь и существовать.
     *               В противном случае вылетит ошибка, и программа закончит свою работу досрочно.
     * @return - объект, который считали с бинарного файла
     */
    private Object readObject(File file) {
        Object object = null;

        try {
            InputStream input = new FileInputStream(file);

            try {
                ObjectInputStream objectInput = new ObjectInputStream(input);

                try {
                    object = objectInput.readObject();
                } finally {
                    objectInput.close();
                }
            } finally {
                input.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return object;
    }

    private ApplicationSaver() {
        state = new HashMap<String, String>();
        restored = false;
    }

    /**
     * <p>Возвращает объект класса если он создан, а если не создан, то перед этим создает его</p>
     * @return - ссылка на единственный объект класса ApplicationSaver в памяти
     */
    public static ApplicationSaver getInstance() {
        if (applicationSaver == null) {
            applicationSaver = new ApplicationSaver();
        }

        return applicationSaver;
    }

    /**
     * <p>Добавляет состояние какого-то <code>ComponentSaver</code> в наше состояние</p>
     * <p><i>Иначе говоря, сливаем под-словарь <code>ComponentSaver</code> с нашим словарем</i></p>
     * @param saver - объект <code>ComponentSaver</code>, который нужно слить с нашим словарем
     */
    public void addState(ComponentSaver saver) {
        Objects.requireNonNull(saver);

        this.putAll(saver);
    }

    /**
     * <p>Получить состояние какого-то под-словаря в нашем</p>
     * @param prefix - префикс, по которому происходит фильтрация
     * @return - готовый под-словарь
     */
    public ComponentSaver getState(String prefix) {
        ComponentSaver saver = new ComponentSaver(prefix);

        for (Entry<String, String> entry : entrySetByPrefix(prefix)) {
            saver.put(entry.getKey(), entry.getValue());
        }

        return saver;
    }

    /**
     * <p>Сохраняет свое состояние в какой-то бинарный файл в папке, которая находится в домашнем каталоге</p>
     */
    public void save() {
        try {
            String homeCatalogPath = System.getProperty("user.home") + "/Robots/";

            File homeCatalog = new File(homeCatalogPath);

            if (!homeCatalog.exists()) {
                boolean createdCatalog = homeCatalog.mkdir();
            }

            File robotsSave = new File(homeCatalogPath + "robotsSave.bin");

            if (!homeCatalog.exists()) {
                boolean createdSave = homeCatalog.createNewFile();
            }

            writeObject(robotsSave, state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Восстанавливает свое состояние из бинарного файла, который находится в соответствующей папке домашнего каталога</p>
     * <p>Если у вас уже есть какое-то состояние в <code>ApplicationSaver</code> или такого файла нет, то восстановление
     * будет прервано</p>
     * @return - возвращает результат работы. <code>true</code> - если восстановление состояния прошло успешно.
     *                                        <code>false</code> - если восстановление состояния не удалось
     */
    public boolean restore() {
        if (state.size() != 0) {
            restored = false;
            return false;
        }

        String homeCatalogPath = System.getProperty("user.home") + "/Robots/";

        File robotsSave = new File(homeCatalogPath + "robotsSave.bin");

        if (!robotsSave.exists()) {
            restored = false;
            return false;
        }

        state.putAll((Map<String, String>) readObject(robotsSave));

        restored = true;

        return true;
    }

    /**
     * Этот метод имеет такой же принцип работы, как в HashMap
     * @param key the key whose associated value is to be returned
     * @return - значение
     */
    @Override
    public String get(Object key) {
        return state.get(key);
    }

    /**
     * Этот метод имеет такой же принцип работы, как в HashMap
     * @param key the key whose associated value is to be returned
     * @return - старое значение
     */
    @Override
    public String put(String key, String value) {
        if (getPrefix(key) == null) {
            throw new SaversException(SaversException.PREFIX_NOT_EXIST_EXCEPTION_CODE);
        }

        return state.put(key, value);
    }

    /**
     * <p>Возвращает множество объектов Entry, которые хранят в себе ключи и значение.</p>
     * @return - множество Entry<String, String> с ключами и их значениями
     */
    @Override
    public Set<Entry<String, String>> entrySet() {
        return state.entrySet();
    }

    /**
     * <p>Возвращает значение по ключу если оно не равно <code>null</code>. Иначе возвратит стандартное значение</p>
     * @param key - ключ, с которым ассоциировано значение
     * @param defaultValue - стандартное значение
     * @return - значение, которое привязано к ключу
     */
    @Override
    public String getOrDefault(Object key, String defaultValue) {
        return state.getOrDefault(key, defaultValue);
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
        state.forEach(action);
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
        state.replaceAll(function);
    }

    /**
     * <p>Кладет значение в словарь если там значение пустое (равно <code>null</code>)</p>
     * @param key - ключ, по которому надо добавить значение в словарь
     * @param value - значение, которое мы хотим присвоить ключу в словаре
     * @return - предыдущее значение
     */
    @Override
    public String putIfAbsent(String key, String value) {
        if (getPrefix(key) == null) {
            throw new SaversException(SaversException.PREFIX_NOT_EXIST_EXCEPTION_CODE);
        }

        return state.putIfAbsent(key, value);
    }

    /**
     * <p>Удаляет запись если совпавшему ключу и значению. Если ключ или значение не совпадает, то запись не будет удалена</p>
     * @param key ключ, по которому надо удалить запись
     * @param value значение, по которому надо удалить запись
     * @return - результат удаления: <code>true</code> или <code>false</code>
     */
    @Override
    public boolean remove(Object key, Object value) {
        return state.remove(key, value);
    }

    /**
     * <p>Заменяет значение по ключу только если оно в момент замены совпадает с <code>oldValue</code></p>
     * @param key - ключ, по которому надо заменить значение
     * @param oldValue - значение, с которым должно совпасть в момент замены
     * @param newValue - новое значение для ключа
     * @return - результат замены: <code>true</code> или <code>false</code>
     */
    @Override
    public boolean replace(String key, String oldValue, String newValue) {
        return state.replace(key, oldValue, newValue);
    }

    /**
     * <p>Заменяет значение записи по ключу только в том случае, когда оно сопоставлено с каким-то значением</p>
     * @param key - ключ, по которому надо заменить значение
     * @param value - значение, которое хотим заменить у ключа
     * @return - предыдущее значение или null
     */
    @Override
    public String replace(String key, String value) {
        return state.replace(key, value);
    }

    /**
     * <p>
     *     Если указанный ключ еще не связан со значением (или сопоставлен нулю), пытается вычислить его значение с помощью заданной функции сопоставления и вводит его в эту карту, если значение не равно null.
     *     Если функция сопоставления возвращает значение <code>null</code>, отображение не записывается. Если функция сопоставления сама выдает (непроверенное) исключение, то исключение генерируется, и сопоставление не записывается.
     *     Наиболее распространенным использованием является создание нового объекта, служащего исходным отображенным значением или сохраненным в памяти результатом
     * </p>
     * @param key - ключ, с которым должно быть связано указанное значение
     * @param mappingFunction функция отображения для вычисления значения
     * @return - текущее (существующее или вычисленное) значение, связанное с указанным ключом, или null, если вычисленное значение равно null
     */
    @Override
    public String computeIfAbsent(String key, Function<? super String, ? extends String> mappingFunction) {
        return state.computeIfAbsent(key, mappingFunction);
    }

    /**
     * <p>
     *      Если значение для указанного ключа присутствует и не равно <code>null</code>, выполняется попытка вычислить новое сопоставление с учетом ключа и его текущего сопоставленного значения.
     *      Если функция переназначения возвращает значение <code>null</code>, сопоставление удаляется. Если функция переназначения сама выдает (непроверенное) исключение, исключение повторно генерируется, а текущее сопоставление остается неизменным
     * </p>
     * @param key - ключ, с которым должно быть связано указанное значение
     * @param remappingFunction - функция переназначения для вычисления значения
     * @return - новое значение, связанное с указанным ключом, или <code>null</code>, если его нет
     */
    @Override
    public String computeIfPresent(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
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
     * @param key - ключ, с которым должно быть связано указанное значение
     * @param remappingFunction - функция переназначения для вычисления значения
     * @return - новое значение, связанное с указанным ключом, или <code>null</code>, если его нет
     */
    @Override
    public String compute(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
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
     * @param key ключ, с которым должно быть связано результирующее значение
     * @param value - ненулевое значение, которое будет объединено с существующим значением
     *              связанный с ключом или, если не существует значения или значение null
     *              связан с ключом, должен быть связан с ключом
     * @param remappingFunction - функция переназначения для повторного вычисления значения, если оно присутствует
     * @return - новое значение, связанное с указанным ключом, или null, если с ключом не связано ни одно значение
     */
    @Override
    public String merge(String key, String value, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
        return state.merge(key, value, remappingFunction);
    }

    /**
     * <p>Проверяет восстановлены ли данные</p>
     * @return - выдает <code>true</code> если восстановлены, и <code>false</code> в противном случае
     */
    public boolean isRestored() {
        return restored;
    }
}
