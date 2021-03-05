import org.junit.*;
import robots.log.LogEntry;
import robots.log.LogLevel;
import robots.log.LogWindowSource;

import java.util.Iterator;


public class LogWindowSourceTest {
    private LogWindowSource logWindowSource;

    @Before
    public void setUp() {
        logWindowSource = new LogWindowSource(2);
    }

    @Test
    public void messagesStoresAsLimitedQueueTest() {
        String firstMsg = "First";
        String secondMsg = "Second";
        String thirdMsg = "Third";
        LogLevel testLogLevel = LogLevel.Debug;
        logWindowSource.append(testLogLevel, firstMsg);
        logWindowSource.append(testLogLevel, secondMsg);
        logWindowSource.append(testLogLevel, thirdMsg);
        Iterator<LogEntry> entriesIterator = logWindowSource.all().iterator();
        LogEntry firstEntry = entriesIterator.next();
        LogEntry secondEntry = entriesIterator.next();
        Assert.assertEquals(secondMsg, firstEntry.getMessage());
        Assert.assertEquals(testLogLevel, firstEntry.getLevel());
        Assert.assertEquals(thirdMsg, secondEntry.getMessage());
        Assert.assertEquals(testLogLevel, secondEntry.getLevel());
    }
}
