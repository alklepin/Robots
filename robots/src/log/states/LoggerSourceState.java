package log.states;

import log.LogEntry;

import java.io.Serializable;
import java.util.ArrayList;

public class LoggerSourceState implements Serializable {
    public ArrayList<LogEntry> logs;
    public int qLength;
    public LoggerSourceState(ArrayList<LogEntry> l,int len){
        logs=l;
        qLength=len;
    }
}
