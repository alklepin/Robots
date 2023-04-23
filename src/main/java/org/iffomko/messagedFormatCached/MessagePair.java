package org.iffomko.messagedFormatCached;

import java.util.Arrays;
import java.util.Objects;

/**
 * <p>
 *     Неизменяемый класс, который хранит в себе входящие паттерн,
 *     по которому будет задаваться строчка, и параметры, которые будут вставляться в этот паттерн
 * </p>
 */
public class MessagePair {
    private final String pattern;
    private final Object[] params;

    /**
     * <p>Инициализирует класс входящими параметрами</p>
     * @param pattern паттерн, по которому будет создавать исходная строчка
     * @param params параметры, которые будут вставляться в этот паттерн
     * @throws IllegalArgumentException выбрасывается эта ошибка, если один из входящих параметров равен null
     */
    public MessagePair(String pattern, Object[] params) {
        if (pattern == null || params == null) {
            throw new IllegalArgumentException("Один из входящих параметров равен null");
        }

        this.pattern = pattern;
        this.params = params;
    }

    /**
     * <p>Проверяет на равенство два класса</p>
     * @param o входящий класс, с которым надо сравнить текущий
     * @return возвращает true, если два класса равны или false, если два класса не равны
     */
    @Override
    public boolean equals(Object o) {
        try {
            MessagePair that = (MessagePair) o;
        } catch (ClassCastException e) {
            return false;
        }

        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        MessagePair that = (MessagePair) o;

        return pattern.equals(that.pattern) && Arrays.equals(params, that.params);
    }

    /**
     * <p>Формирует и возвращает хеш этого класса</p>
     * @return хеш-код
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(pattern);

        result = 31 * result + Arrays.hashCode(params);

        return result;
    }
}
