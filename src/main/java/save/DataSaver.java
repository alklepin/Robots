package save;

import log.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSaver {
    String filename = System.getProperty("user.home") + "/RobotData.txt";

    /**
     * Store saved data locally
     */
    public void store(StateManager stateManager) {
        Logger.debug("Store trigger");
        try {
            Path file = Path.of(filename);
            List<String> data = new ArrayList<>();
            Map<String, String> stateStorage = stateManager.extractStorage();
            for (String key : stateStorage.keySet()) {
                data.add(key + "=" + stateStorage.get(key));
            }
            Files.write(file, data);
        } catch (IOException e) {
            Logger.debug("Failed to store data due to IO exception with message: \n" + e.getMessage());
        }
    }

    /**
     * Restore locally saved data
     */
    public StateManager restore() {
        Logger.debug("Restore trigger");
        StateManager restored = new StateManager();
        try {
            String[] data = Files.readString(Path.of(filename)).split("\n");
            String[] parsedLine;
            for (String line : data) {
                parsedLine = line.split("=");
                restored.put(parsedLine[0], parsedLine[1]);
            }
        } catch (IOException e) {
            Logger.debug("Failed to restore data due to IO exception with message: \n" + e.getMessage());
        }
        return restored;
    }
}
