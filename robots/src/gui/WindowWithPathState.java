package gui;

import javax.swing.*;
import java.io.*;

public abstract class WindowWithPathState extends JInternalFrame implements IObjectState{
    public WindowWithPathState(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
        super(title, resizable, closable, maximizable, iconifiable);
    }

    protected void save(String filename){
        File file = new File(filename);
        try (OutputStream os = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
            oos.writeObject(getName());
            oos.writeObject(getLocation().x);
            oos.writeObject(getLocation().y);
            oos.writeObject(getHeight());
            oos.writeObject(getWidth());
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    protected void load(String filename){
        try {
            InputStream is = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
            try {
                setName((String) ois.readObject());
                setLocation((Integer) ois.readObject(), (Integer) ois.readObject());
                setSize((Integer) ois.readObject(), (Integer) ois.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
