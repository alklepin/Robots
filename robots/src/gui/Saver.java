package gui;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.*;

public class Saver {


    public static void serialize(Object info, String name) {
        File file = new File(name);
        try (OutputStream os = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(os))) {
            oos.writeObject(info);
            oos.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static WindowData deserialize(File file){
        WindowData windowData = null;
        try {
            InputStream is = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
            try {
                windowData = (WindowData) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return windowData;
    }

    public static void restoreWindow(JInternalFrame frame, WindowData windowData){
        frame.setLocation(windowData.positionX, windowData.positionY);
        frame.setSize(windowData.width, windowData.height);
        try {
            frame.setMaximum(windowData.isMax);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        try {
            frame.setIcon(windowData.isMin);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

}
