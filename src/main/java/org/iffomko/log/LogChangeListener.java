package org.iffomko.log;

/**
 * Интерфейс, который определяет структуру обработчиков логов
 */
public interface LogChangeListener
{
    /**
     * Метод, который будет вызываться при изменении в логах
     */
    public void onLogChanged();
}
