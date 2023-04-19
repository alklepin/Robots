package core;

import log.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {
    private static FileService fileService;

    public void uploadObjectToFile(String path, String name, Object object) {
        if (!checkPathExist(path) && !createFolder(path)) {
            Logger.error("Can't create folder -> " + path);
            return;
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path + "\\" + name)))) {
            out.writeObject(object);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object readObjectFromFile(String path, String name) {
        if (!checkPathExist(path) && !createFolder(path)) {
            Logger.error("Can't create folder -> " + path);
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path + "\\" + name)))) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            Logger.error("Can't load object -> " + name);
            return null;
        }
    }

    private boolean checkPathExist(String path) {
        return Files.exists(Path.of(path));
    }

    private boolean createFolder(String path) {
        return new File(path).mkdirs();
    }

    private FileService() {

    }

    public static FileService getFileService() {
        if (fileService == null)
            fileService = new FileService();

        return fileService;
    }
}
