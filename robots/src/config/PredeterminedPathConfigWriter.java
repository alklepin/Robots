package config;

import java.io.*;
import java.nio.file.Paths;

public class PredeterminedPathConfigWriter implements ConfigWriter {

    File m_path;
    ObjectOutputStream m_stream;

    public PredeterminedPathConfigWriter(String path) {
        m_path = new File(path);
        try{
            m_path.createNewFile();
            m_stream=new ObjectOutputStream(new FileOutputStream(m_path));


        } catch (IOException e) {
            m_stream=null;
        }
    }

    @Override
    public void writeObject(Object data) throws IOException {
        if (m_stream == null) {
            throw new ConfigException();
        }

        m_stream.writeObject(data);
    }

    @Override
    public void close() throws IOException {
        if (m_stream == null) {
            return;
        }
        m_stream.close();
    }


}
