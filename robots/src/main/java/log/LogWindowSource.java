package main.java.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogWindowSource {
    private final int mIQueueLength;
    private final ArrayList<LogEntry> mMessages;
    private final CopyOnWriteArrayList<LogChangeListener> mListeners;
    private volatile LogChangeListener[] mActivelisteners;

    public LogWindowSource(int iQueueLength) {
        this.mIQueueLength = iQueueLength;
        this.mMessages = new ArrayList(iQueueLength);
        this.mListeners = new CopyOnWriteArrayList();
    }

    public void registerListener(LogChangeListener listener) {
        this.mListeners.add(listener);
        this.mActivelisteners = null;
    }

    public void append(LogLevel logLevel, String strMessage) {
        LogEntry entry = new LogEntry(logLevel, strMessage);
        if (this.size() >= this.mIQueueLength) {
            this.mMessages.remove(0);
        }

        synchronized(this) {
            this.mMessages.add(entry);
        }

        LogChangeListener[] activeListeners = this.mActivelisteners;
        if (activeListeners == null) {
            synchronized(this.mListeners) {
                if (this.mActivelisteners == null) {
                    activeListeners = (LogChangeListener[])this.mListeners.toArray(new LogChangeListener[0]);
                    this.mActivelisteners = activeListeners;
                }
            }
        }

        LogChangeListener[] var5 = activeListeners;
        int var6 = activeListeners.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            LogChangeListener listener = var5[var7];
            listener.onLogChanged();
        }

    }

    public synchronized int size() {
        return this.mMessages.size();
    }

    public synchronized Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom >= 0 && startFrom < this.size()) {
            int indexTo = Math.min(startFrom + count, this.size());
            return this.mMessages.subList(startFrom, indexTo);
        } else {
            return Collections.emptyList();
        }
    }

    public synchronized Iterable<LogEntry> all() {
        return this.mMessages;
    }
}
