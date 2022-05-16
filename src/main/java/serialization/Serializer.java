package serialization;

import java.io.*;

public class Serializer {
    static void save(Serializable serializable, String path) {
        try (var output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)))){
            output.writeObject(serializable);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    static Object load(String path) {
        var file = new File(path);

        if (file.isFile()){
            try (var input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                return input.readObject();
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }

        return null;
    }
}
