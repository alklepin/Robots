package serialization;

import gui.MainApplicationFrame;

import javax.swing.*;

public class StorageHelper {
    private final WindowStorage windowStorage;

    public StorageHelper(WindowStorage _windowStorage) {
        this.windowStorage = _windowStorage;
    }

    public void restoreWindows(JFrame mainFrame, JInternalFrame[] windowsToRestore) {
        this.windowStorage.restore(mainFrame.getClass().toString(), mainFrame);
        for(int i = 0; i < windowsToRestore.length; i++) {
            this.windowStorage.restore(windowsToRestore[i].getClass().toString(), windowsToRestore[i]);
        }
    }
    public void storeWindows(JFrame mainFrame, JInternalFrame[] windowsToRestore) {
        this.windowStorage.store(mainFrame.getClass().toString(), mainFrame);
        for(int i = 0; i < windowsToRestore.length; i++) {
            this.windowStorage.store(windowsToRestore[i].getClass().toString(), windowsToRestore[i]);
        }
        this.windowStorage.save();
    }
}