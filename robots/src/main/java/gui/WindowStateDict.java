package gui;

import java.util.HashMap;
import java.util.Map;

public class WindowStateDict {
    private Map<String, DictState> map;

    public WindowStateDict() {
        map = new HashMap<>();
    }

    public void unite(String name, DictState dictState) {
        map.put(name, dictState);
    }

    public DictState getDictStateByName(String name) {
        return map.get(name);
    }

    public void writeStateToFile() {
        FileManager saveToFile = new FileManager();
        saveToFile.write(map);
    }

    public void readStateFromFile() {
        FileManager saveToFile = new FileManager();
        map = saveToFile.read();
    }
}
