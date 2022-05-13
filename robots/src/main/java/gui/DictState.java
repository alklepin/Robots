package gui;

import java.util.HashMap;
import java.util.Map;

public class DictState {
    private Map<String, String> map;

    public DictState() {
        map = new HashMap<>();
    }

    public void addState(String name, String value) {
        map.put(name, value);
    }

    public Map<String, String> getDictState() {
        return map;
    }
}
