package gui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileManager {
    public void write(Map<String, DictState> map) {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(
                "windows_states.txt",
                true))) {
            for (Map.Entry<String, DictState> entry : map.entrySet()) {
                Map<String, String> internalMap = entry.getValue().getDictState();
                for (Map.Entry<String, String> internalEntry : internalMap.entrySet()) {
                    writer.append(String.format("%s:%s:%s", entry.getKey(),
                            internalEntry.getKey(), internalEntry.getValue()));
                    writer.println("");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Map<String, DictState> read() {
        Map<String, DictState> map = new HashMap<>();
        DictState formDictState = new DictState();
        String prevName;
        try (Scanner scanner = new Scanner(
                new File(String.format("%s/windows_states.txt", System.getProperty("user.dir"))))) {
            while (scanner.hasNext()) {
                String[] info = scanner.nextLine().split(":");
                prevName = info[0];
                if (!map.containsKey(info[0]) && formDictState.getDictState().size() != 0) {
                    formDictState.addState(info[1], info[2]);
                    map.put(prevName, formDictState);
                    formDictState = new DictState();
                }
                else {
                    formDictState.addState(info[1], info[2]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    public boolean isExist(String name) {
        File f = new File(String.format("%s/%s", System.getProperty("user.dir"), name));
        return f.exists() && !f.isDirectory();
    }
}

