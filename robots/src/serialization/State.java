package serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface State extends Serializable {
    // Копирование состояния объекта
    void copy(Object obj);

    // Сохранение объекта в файл. На вход принимается путь к файлу, в который нужно сохранить
    // состояние объекта
    default void saveState(String outPath) {
        try (ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(outPath))) {
            outStream.writeObject(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Загрузка состояния объекта из файла. На вход так же принимается путь к файлу,
    // из которого будет загружено состояние
    default void loadState(String inPath) {
        if (new File(inPath).isFile()) {
            try (ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(inPath))) {
                Object object = inStream.readObject();
                copy(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
