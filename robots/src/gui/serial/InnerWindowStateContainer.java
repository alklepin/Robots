package gui.serial;

import javax.swing.*;
import java.io.Serializable;

public class InnerWindowStateContainer implements Serializable {


    public int x;
    public int y;
    public int sizeX;
    public int sizeY;



    public InnerWindowStateContainer(int x, int y, int sizeX, int sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;

    }
    public void applyState(JInternalFrame frame){
        frame.setLocation(x,y);
        frame.setSize(sizeX,sizeY);
    }








}
