package org.iffomko.savers;

import java.io.*;
import java.util.*;

/**
 * <p>Хранит в себе состояние всех настроек с префиксами и умеет сохранять свое состояние в файл</p>
 */
public class StateKeeper {
    private static StateKeeper stateKeeper = null;
    private final Map<String, String> state;

    /**
     * <p>Собирает и возвращает множество всех объектов, которые содержат ключи с определенным префиксом и их значение</p>
     * @param prefix - префикс, по которому будут фильтроваться ключи
     * @return - множество всех Entry<String, String>
     */
    private Set<Map.Entry<String, String>> entrySet(String prefix) {
        Set<Map.Entry<String, String>> response = new HashSet<Map.Entry<String, String>>();

        for (Map.Entry<String, String> entry : state.entrySet()) {
            if (entry.getKey().startsWith(prefix)) {
                response.add(entry);
            }
        }

        return response;
    }

    private StateKeeper() {
        state = new HashMap<String, String>();
    }

    /**
     * <p>Возвращает объект класса если он создан, а если не создан, то перед этим создает его</p>
     * @return - ссылка на единственный объект класса StateKeeper в памяти
     */
    public static StateKeeper getInstance() {
        if (stateKeeper == null) {
            synchronized (StateKeeper.class) {
                if (stateKeeper == null) {
                    stateKeeper = new StateKeeper();
                }
            }
        }

        return stateKeeper;
    }

    /**
     * <p>Добавляет состояние какого-то класса, который реализует интерфейс <code>Savable</code>, в наше состояние</p>
     * <p><i>Иначе говоря, сливаем под-словарь <code>Savable</code> с нашим словарем</i></p>
     * @param obj - объект, который реализует интерфейс <code>Savable</code>, который нужно слить с нашим словарем
     */
    public void addState(Savable obj) throws SaverException {
        if (obj == null) {
            throw new SaverException("Передаваемый объект, который реализует интерфейс Savable, равен null");
        }

        String prefix = obj.getPrefix();

        if (prefix == null) {
            throw new SaverException("Значение префикса объекта, который был передан в параметрах, равно null");
        }

        for (Map.Entry<String, String> entry : obj.save().entrySet()) {
            String key = prefix + "." + entry.getKey();
            String value = entry.getValue();

            state.put(key, value);
        }
    }

    /**
     * <p>Получить состояние какого-то под-словаря в нашем</p>
     *
     * @param prefix - префикс, по которому происходит фильтрация
     * @return - готовый под-словарь
     */
    public Map<String, String> getState(String prefix) throws SaverException {
        if (prefix == null) {
            throw new SaverException("Значение префикса объекта, который был передан в параметрах, равно null");
        }

        Map<String, String> response = new HashMap<>();

        for (Map.Entry<String, String> entry : entrySet(prefix)) {
            response.put(entry.getKey().substring(prefix.length() + 1), entry.getValue());
        }

        return response;
    }

    /**
     * <p>Сохраняет свое состояние в какой-то бинарный файл в папке, которая находится в домашнем каталоге</p>
     * <p>Если сохранение производится в первый раз, то </p>
     */
    public void save() {
        Saver<Map<String, String>> saver = new Saver<>();

        String homeCatalogPath = System.getProperty("user.home") + "/Robots/";

        File homeCatalog = new File(homeCatalogPath);

        if (!homeCatalog.exists()) {
            boolean createdCatalog = homeCatalog.mkdir();
        }

        File robotsSave = new File(homeCatalogPath + "robotsSave.bin");

        try {
            if (!homeCatalog.exists()) {
                boolean createdSave = homeCatalog.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            saver.writeObject(robotsSave, state);
        } catch (SaverException e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Восстанавливает свое состояние из бинарного файла, который находится в соответствующей папке домашнего каталога</p>
     * @return - возвращает результат работы. <code>true</code> - если восстановление состояния прошло успешно.
     *                                        <code>false</code> - если восстановление состояния не удалось
     */
    public boolean restore() {
        Saver<Map<String, String>> saver = new Saver<>();

        String homeCatalogPath = System.getProperty("user.home") + "/Robots/";

        File homeDir = new File(homeCatalogPath);

        if (!homeDir.exists()) {
            return false;
        }

        File robotsSave = new File(homeCatalogPath + "robotsSave.bin");

        if (!robotsSave.exists()) {
            return false;
        }

        try {
            state.putAll(saver.readObject(robotsSave));
        } catch (SaverException e) {
            e.printStackTrace();
        }

        return true;
    }
}
