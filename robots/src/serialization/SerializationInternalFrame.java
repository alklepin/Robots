package serialization;

import java.beans.PropertyVetoException;
import java.io.File;
import javax.swing.JInternalFrame;

public class SerializationInternalFrame extends JInternalFrame {

    public SerializationInternalFrame(String title, boolean b, boolean b1, boolean b2, boolean b3) {
        super(title, b, b1, b2, b3);
    }
    // Возвращает объект класса Frame с текущим состоянием окна
    public Frame getFrameState(){
        return new Frame(this.getSize(), this.getLocation(), this.isIcon, this.isMaximum, this.isSelected);
    }
    // Устанавливает текцщие характеристики окна
    public void setFrameState(Frame frame){
        try{
            this.setSize(frame.dim);
            this.setLocation(frame.location);
            this.setIcon(frame.isIcon);
            this.setMaximum(frame.isMaximum);
            this.setSelected(frame.isSelected);
        } catch (PropertyVetoException e){
            e.printStackTrace();
        }

    }
    // Сохранение состояния окна
    public void save(String outPath) {
        getFrameState().saveState(outPath);
    }

    // Загрузка состояния окна
    public void load(String inPath) {
        if (new File(inPath).isFile()) {
            Frame frameState = new Frame();
            frameState.loadState(inPath);
            setFrameState(frameState);
        }
    }
}
