package gui;

public interface Recoverable {
    void saveState(int width, int height);
    DictState getRecoveryState();
}
