package config;

import java.io.Closeable;
import java.io.IOException;

public interface ConfigWriter extends Closeable {
    void writeObject(Object data) throws IOException;

}
