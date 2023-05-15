package config;

import java.io.*;

public class PredeterminedPathConfigReader implements ConfigReader{

    private File m_path;
    private ObjectInputStream m_data;

    public PredeterminedPathConfigReader(String path){
        m_path=new File(path);
        try{
            m_data=new ObjectInputStream( new BufferedInputStream(new FileInputStream(m_path)));
        }
        catch (IOException e){
            m_data=null;
        }
    }



    @Override
    public void close() throws IOException {
        if(m_data==null){
            return;
        }
        m_data.close();
    }

    @Override
    public Object readObject() throws ConfigException, IOException, ClassNotFoundException {
        if(m_data==null){
            throw new ConfigException();
        }
        return m_data.readObject();
    }
}
