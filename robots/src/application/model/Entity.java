package application.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public interface Entity extends PropertyChangeListener
{
    void update();

    void onStart(PropertyChangeSupport publisher);

    void onFinish(PropertyChangeSupport publisher);
}
