package main.java.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public interface Entity extends PropertyChangeListener {
    void update();

    default void onStart(PropertyChangeSupport var1) {

    }
}
