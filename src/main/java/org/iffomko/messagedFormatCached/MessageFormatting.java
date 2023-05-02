package org.iffomko.messagedFormatCached;

import org.iffomko.gui.localization.Localization;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Класс, который оптимизирует статический метод format у класса
 *     <code>MessageFormat</code> за счет кеширования входящих параметров их результатов,
 *     а также расширяет функционал для хранения названия каких-то элементов, как ключ, и их локализацию
 *     с помощью <code>Localization</code>
 * </p>
 */
public class MessageFormatting {
    private final static Map<MessagePair, String> CACHE_BY_MESSAGE_FORMAT = new HashMap<>();
    private final static Map<String, String> USUALLY_CACHE = new HashMap<>();

    private static volatile MessageFormatting INSTANCE;
    private static final Object synchronizedObject = new Object();

    private MessageFormatting() {}

    /**
     * Возвращает единственный экземпляр объекта <code>MessageFormatting</code> в памяти
     * @return экземпляр объекта <code>MessageFormatting</code>
     */
    public static MessageFormatting getInstance() {
        if (INSTANCE == null) {
            synchronized (synchronizedObject) {
                if (INSTANCE == null) {
                    INSTANCE = new MessageFormatting();
                }
            }
        }

        return INSTANCE;
    }

    /**
     * <p>
     *     Смотрит в кеше был ли уже похожий запрос. Если был, то возвращает строчку из кеша, иначе
     *     формирует новую строчку по паттерну и параметрам с помощью класс <code>MessageFormat</code>
     * </p>
     * @param pattern паттерн, с помощью которого нужно сформировать строчку
     * @param params параметры, которые нужно вставить в шаблонную строчку
     * @return строка созданная по паттерну, содержащая те параметры, которые были указаны
     */
    public String format(String pattern, Object[] params) {
        MessagePair temp = new MessagePair(pattern, params);

        synchronized (CACHE_BY_MESSAGE_FORMAT) {
            if (CACHE_BY_MESSAGE_FORMAT.get(temp) != null) {
                return CACHE_BY_MESSAGE_FORMAT.get(temp);
            }

            String result = MessageFormat.format(pattern, params);

            CACHE_BY_MESSAGE_FORMAT.put(temp, result);

            return result;
        }
    }

    /**
     * <p>
     *     Смотрит в кеше был ли уже похожий запрос. Если был, то возвращает локализированную строчку из кеша, иначе
     *     достает локализацию этого элемента из пакета
     * </p>
     * @param name имя, которое надо локализовать
     * @param packet пакет, из которого можно достать локализацию элемента
     * @return строка созданная по паттерну
     */
    public String format(String name, String packet) {
        synchronized (USUALLY_CACHE) {
            if (USUALLY_CACHE.get(name) != null) {
                return USUALLY_CACHE.get(name);
            }

            String response = Localization.getInstance().getResourceBundle(packet).getString(name);

            USUALLY_CACHE.put(name, response);

            return response;
        }
    }

    /**
     * <p>
     *     Очищает кеш у <code>MessageFormatting</code>
     * </p>
     */
    public void clearCache() {
        CACHE_BY_MESSAGE_FORMAT.clear();
        USUALLY_CACHE.clear();
    }
}
