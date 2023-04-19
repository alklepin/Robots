package core;

public class UserDataSaver {
    private static UserDataSaver userDataSaver;
    private final String userDocumentsPath;

    public void saveObject(String name, Object object) {
        FileService.getFileService().uploadObjectToFile(userDocumentsPath, name, object);
    }

    public Object loadObject(String name) {
        return FileService.getFileService().readObjectFromFile(userDocumentsPath, name);
    }

    private UserDataSaver() {
        userDocumentsPath = System.getProperty("user.home") + "\\Documents\\Robots";
    }

    public static UserDataSaver getUserDataSaver() {
        if (userDataSaver == null)
            userDataSaver = new UserDataSaver();

        return userDataSaver;
    }
}
