package gui;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileManager {

    public FileManager(String mode) {
        if (mode == "write") {
            try {
                Files.deleteIfExists(Paths.get(String.format("%s/windows_states.txt", System.getProperty("user.dir"))));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        else if (mode == "read" && !isExist("windows_states.txt")) {
            Map<String, DictState> map = new HashMap<>();
            DictState dictState = new DictState();
            dictState.addState("height", "300");
            dictState.addState("width", "300");
            dictState.addState("x", "20");
            dictState.addState("y", "20");
            dictState.addState("is_icon", "false");
            map.put("model", dictState);
            map.put("log", dictState);
            write(map);
        }
    }

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
        String prevName = null;
        try (Scanner scanner = new Scanner(
                new File(String.format("%s/windows_states.txt", System.getProperty("user.dir"))))) {
            while (scanner.hasNext()) {
                String[] info = scanner.nextLine().split(":");
                if (prevName != null && !prevName.equals(info[0])) {
                    map.put(prevName, formDictState);
                    formDictState = new DictState();
                }
                formDictState.addState(info[1], info[2]);
                prevName = info[0];
            }
            map.put(prevName, formDictState);
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

