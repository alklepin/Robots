package log;

import static gui.Constants.LoggerConstants.LOGGER_IQUEUE_LENGTH;

public final class Logger
{
    private static final LogWindowSource defaultLogSource = new LogWindowSource(LOGGER_IQUEUE_LENGTH);
    
    private Logger()
    {
    }

    public static void debug(String strMessage)
    {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }
    
    public static void error(String strMessage)
    {
        defaultLogSource.append(LogLevel.Error, strMessage);
    }

    public static LogWindowSource getDefaultLogSource()
    {
        return defaultLogSource;
    }
}
