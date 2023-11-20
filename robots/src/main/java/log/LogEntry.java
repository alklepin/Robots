package main.java.log;

public class LogEntry {
    private LogLevel mLogLevel;
    private String mStrMessage;

    public LogEntry(LogLevel logLevel, String strMessage) {
        this.mStrMessage = strMessage;
        this.mLogLevel = logLevel;
    }

    public String getMessage() {
        return this.mStrMessage;
    }

    public LogLevel getLevel() {
        return this.mLogLevel;
    }
}