/*package testsLogs;
import log.LogEntry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import log.LogWindowSource;
import log.LogChangeListener;


public class LogWindowSourceTest {

    public enum LogLevel {
        INFO, DEBUG, WARN, ERROR
    }
    @Test
    public void testLogWindowSource() {
        // Create LogWindowSource with a queue length of 5
        LogWindowSource logWindowSource = new LogWindowSource(5);

        // Create and register listeners
        LogChangeListener listener1 = new TestLogChangeListener();
        LogChangeListener listener2 = new TestLogChangeListener();
        logWindowSource.registerListener(listener1);
        logWindowSource.registerListener(listener2);

        // Append log entries
        logWindowSource.append(LogLevel.INFO, "Message 1");
        logWindowSource.append(LogLevel.ERROR, "Message 2");
        logWindowSource.append(LogLevel.DEBUG, "Message 3");

        // Check if listeners were notified
        assertTrue(((TestLogChangeListener) listener1).wasNotified());
        assertTrue(((TestLogChangeListener) listener2).wasNotified());

        // Unregister one listener and append more log entries
        logWindowSource.unregisterListener(listener1);
        logWindowSource.append(LogLevel.WARN, "Message 4");
        logWindowSource.append(LogLevel.INFO, "Message 5");

        // Check if the unregistered listener was not notified
        assertFalse(((TestLogChangeListener) listener1).wasNotified());

        // Check the size of the log messages
        assertEquals(4, logWindowSource.size());

        // Check the range and all methods
        assertEquals(2, logWindowSource.range(1, 2).size());
        assertEquals(4, logWindowSource.all().size());
    }

    // TestLogChangeListener class for testing
    private static class TestLogChangeListener implements LogChangeListener {
        private boolean notified = false;

        @Override
        public void onLogChanged() {
            notified = true;
        }

        public boolean wasNotified() {
            return notified;
        }
    }
}

 */
