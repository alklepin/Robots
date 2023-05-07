package gui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.io.*;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements IObjectState
{
    private final GameVisualizer m_visualizer;
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
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
