package ru.kemichi.robots.log;

public class LogEntry
{
    private LogLevel logLevel;
    private String m_strMessage;
    
    public LogEntry(LogLevel logLevel, String strMessage)
    {
        m_strMessage = strMessage;
        this.logLevel = logLevel;
    }
    
    public String getMessage()
    {
        return m_strMessage;
    }
    
    public LogLevel getLevel()
    {
        return logLevel;
    }
}

