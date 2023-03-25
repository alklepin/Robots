package org.iffomko.savers;

import java.io.*;
import java.nio.file.Files;

/**
 * <p>Умеет сохранять объекты в бинарный файл и восстанавливать его из файла</p>
 */
public class Saver<T> {
    /**
     * <p>Пишет объект в бинарный файл</p>
     * @param file - бинарный файл, в который надо записать объект. Файл должен иметь корректный путь и существовать.
     *               В противном случае вылетит ошибка, и программа закончит свою работу досрочно.
     * @param object - объект, который надо записать в бинарный файл
     */
    public void writeObject(File file, T object) throws SaverException {
        try {
            try (OutputStream output = Files.newOutputStream(file.toPath())) {
                try (ObjectOutputStream objectOutput = new ObjectOutputStream(output)) {
                    objectOutput.writeObject(object);
                    objectOutput.flush();
                }
            }
        } catch (IOException e) {
            throw new SaverException(e.getMessage());
        }
    }

    /**
     * <p>Считывает объект с бинарного файла и возвращает его</p>
     * @param file - бинарный файл, с которого надо считать объект. Файл должен иметь корректный путь и существовать.
     *               В противном случае вылетит ошибка, и программа закончит свою работу досрочно.
     * @return - объект, который считали с бинарного файла
     */
    public T readObject(File file) throws SaverException {
        T object = null;

        try {
            try (InputStream input = Files.newInputStream(file.toPath())) {

                try (ObjectInputStream objectInput = new ObjectInputStream(input)) {
                    object = (T) objectInput.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new SaverException(e.getMessage());
        }

        return object;
    }
}
