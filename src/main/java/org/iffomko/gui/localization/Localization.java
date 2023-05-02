package org.iffomko.gui.localization;

import java.util.Locale;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * Этот класс отвечает за локализацию всего приложения.
 * При смене локали уведомляются все подписчики этого класса.
 * Спроектирован этот класс с помощью паттерна "Наблюдаемый-Наблюдатель" и "Синглтон"
 */
public class Localization extends Observable {
    private Locale locale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
    private static volatile Localization INSTANCE;
    private static final Object synchronizationObject = new Object();
    public static final String KEY_LOCAL_CHANGED = "Локаль изменилась";

    /**
     * Изменяет локаль для локализации приложения и уведомляет о смене локали всех подписчиков
     * @param locale новая локаль
     */
    public void setLocale(Locale locale) {
        if (locale != null) {
            this.locale = locale;

            setChanged();
            notifyObservers(KEY_LOCAL_CHANGED);
            clearChanged();
        }
    }

    /**
     * Возвращает объект типа <code>ResourceBundle</code>, который использует локализацию некоторого пакета
     * @param packet пакет, который используется для локализации
     * @return экземпляр класса <code>ResourceBundle</code>
     */
    public ResourceBundle getResourceBundle(String packet) {
        return ResourceBundle.getBundle(
                packet,
                locale
        );
    }

    /**
     * Создает единственный экземпляр класса <code>Localization</code> в памяти и возвращает его
     * @return экземпляр класса <code>Localization</code>
     */
    public static Localization getInstance() {
        if (INSTANCE == null) {
            synchronized (synchronizationObject) {
                if (INSTANCE == null) {
                    INSTANCE = new Localization();
                }
            }
        }

        return INSTANCE;
    }
}
