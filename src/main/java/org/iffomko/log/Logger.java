package org.iffomko.log;

/**
 * Класс, который порождает в памяти программы только один объект, сохраняющий в себе логи.
 * Выполнен в стиле паттерна-проектирования Singleton
 */
public final class Logger
{
    private static final LogWindowSource defaultLogSource;
    static {
        defaultLogSource = new LogWindowSource(100);
    }

    /**
     * Запрещает создание экземпляра этого класса в памяти программы
     */
    private Logger()
    {
    }

    /**
     * Добавляет в объект с логами сообщение для дебага
     * @param strMessage - сообщение лога
     */
    public static void debug(String strMessage)
    {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }

    /**
     * Добавляет в объект с логами сообщение об ошибке
     * @param strMessage
     */
    public static void error(String strMessage)
    {
        defaultLogSource.append(LogLevel.Error, strMessage);
    }

    /**
     * Возвращает объект со всеми логами
     * @return - объект LogWindowSource со всеми логами
     */
    public static LogWindowSource getDefaultLogSource()
    {
        return defaultLogSource;
    }
}
