package org.iffomko.savers;

import java.util.Map;

/**
 * <p>Описывает способность класса к сохранению</p>
 */
public interface Savable {
    /**
     * <p>Возвращает состояние настроек класса, который нужно сохранить</p>
     * @return - любой объект, который реализует интерфейс <code>Map</code>
     */
    Map<String, String> save();

    /**
     * <p>Восстанавливает состояние объекта исходя из состояния, которое пришло ему извне</p>
     * @param state - состояние, которое нужно восстановить
     */
    void restore(Map<String, String> state);

    /**
     * <p>Возвращает префикс, который ассоциируется с этим классом</p>
     * @return - префикс
     */
    String getPrefix();
}
