package serialization;

import javax.swing.*;
import java.util.HashMap;

public class WindowStorage {
    private final String path;
    private boolean is_restored;

    private HashMap<String, ComponentDescriber> frames;

    public WindowStorage(String path) {
        this.path = path;
        frames = (HashMap<String, ComponentDescriber>) Serializer.load(path);

        if (frames == null) {
            frames = new HashMap<>();
        } else {
            is_restored = true;
        }
    }

    public void store(String key, JFrame frame) { frames.put(key, new FrameDescriber(frame)); }

    public void store(String key, JInternalFrame frame) { frames.put(key, new InternalFrameDescriber(frame)); }

    public void restore(String key, JFrame frame) {
        var describer = (FrameDescriber) frames.getOrDefault(key, null);
        if (describer != null) { describer.restoreState(frame); }
    }

    public void restore(String key, JInternalFrame frame) {
        var describer = (InternalFrameDescriber) frames.getOrDefault(key, null);
        if (describer != null) { describer.restoreState(frame); }
    }

    public void save() { Serializer.save(frames, path); }

    public boolean isRestored() { return is_restored; }
}
