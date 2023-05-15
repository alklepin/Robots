package config;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;

public interface ConfigReader extends Closeable {
    Object readObject() throws ConfigException, IOException, ClassNotFoundException;
}
