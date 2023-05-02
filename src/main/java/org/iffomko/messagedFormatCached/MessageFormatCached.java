package org.iffomko.messagedFormatCached;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *     Класс, который оптимизирует статический метод format у класса
 *     <code>MessageFormat</code> за счет кеширования входящих параметров их результатов
 * </p>
 */
public class MessageFormatCached {
    private final static Map<MessagePair, String> CACHED = new HashMap<>();

    /**
     * <p>
     *     Смотрит в кеше был ли уже похожий запрос. Если был, то возвращает строчку из кеша, иначе
     *     формирует новую строчку по паттерну и параметрам с помощью класс <code>MessageFormat</code>
     * </p>
     * @param pattern паттерн, с помощью которого нужно сформировать строчку
     * @param params параметры, которые нужно вставить в шаблонную строчку
     * @return строка созданная по паттерну, содержащая те параметры, которые были указаны
     */
    public static String format(String pattern, Object[] params) {
        MessagePair temp = new MessagePair(pattern, params);

        synchronized (CACHED) {
            if (CACHED.get(temp) != null) {
                return CACHED.get(temp);
            }

            String result = MessageFormat.format(pattern, params);

            CACHED.put(temp, result);

            return result;
        }
    }

    /**
     * <p>
     *     Смотрит в кеше был ли уже похожий запрос. Если был, то возвращает строчку из кеша, иначе
     *     формирует новую строчку по паттерну с помощью класс <code>MessageFormat</code>
     * </p>
     * @param pattern паттерн, с помощью которого нужно сформировать строчку
     * @return строка созданная по паттерну
     */
    public static String format(String pattern) {
        return format(pattern, new Object[0]);
    }
}
