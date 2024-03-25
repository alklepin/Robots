package testsLogs;
import log.LogWindowSource;
import log.LogLevel;
import log.LogEntry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LogWindowSourceTest {

    @Test
    public void testLogAppending() {
        LogWindowSource logWindow = new LogWindowSource(5); // Создаем лог с ограничением в 5 сообщений
        logWindow.append(LogLevel.Info, "Message 1");
        logWindow.append(LogLevel.Warning, "Message 2");
        logWindow.append(LogLevel.Error, "Message 3");

        assertEquals(3, logWindow.size()); // Проверяем, что размер лога равен 3 после добавления 3 сообщений
    }

    @Test
    public void testLogLimit() {
        LogWindowSource logWindow = new LogWindowSource(3); // Создаем лог с ограничением в 3 сообщения
        logWindow.append(LogLevel.Info, "Message 1");
        logWindow.append(LogLevel.Warning, "Message 2");
        logWindow.append(LogLevel.Error, "Message 3");
        logWindow.append(LogLevel.Debug, "Message 4"); // Превышаем лимит, первое сообщение должно быть удалено

        assertEquals(3, logWindow.size()); // Проверяем, что размер лога равен 3 после добавления 4 сообщений
        assertEquals("Message 2", logWindow.all().iterator().next().getMessage()); // Проверяем, что первое сообщение было удалено
    }

    @Test
    public void testLogRange() {
        LogWindowSource logWindow = new LogWindowSource(5); // Создаем лог с ограничением в 5 сообщений
        logWindow.append(LogLevel.Info, "Message 1");
        logWindow.append(LogLevel.Warning, "Message 2");
        logWindow.append(LogLevel.Error, "Message 3");
        logWindow.append(LogLevel.Debug, "Message 4");
        logWindow.append(LogLevel.Info, "Message 5");

        Iterable<LogEntry> range = logWindow.range(1, 3); // Получаем диапазон сообщений с индекса 1 до 3

        int count = 0;
        for (LogEntry entry : range) {
            count++;
        }
        assertEquals(3, count); // Проверяем, что в полученном диапазоне содержится 3 сообщения
    }

    @Test
    public void testLogRangeOutOfBounds() {
        LogWindowSource logWindow = new LogWindowSource(5); // Создаем лог с ограничением в 5 сообщений
        logWindow.append(LogLevel.Info, "Message 1");

        Iterable<LogEntry> range = logWindow.range(2, 3); // Пытаемся получить диапазон с индекса 2, хотя в логе только 1 сообщение

        int count = 0;
        for (LogEntry entry : range) {
            count++;
        }
        assertEquals(0, count); // Проверяем, что полученный диапазон пустой
    }
}
