package gui;

import localization.LocalizationManager;
import model.RobotModel;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RobotCoordinatesWindow extends JInternalFrame implements PropertyChangeListener {
    private final JLabel positionLabel;
    private final JLabel directionLabel;
    private final RobotModel model;
    private final LocalizationManager loc = LocalizationManager.getInstance();

    public RobotCoordinatesWindow(RobotModel model) {
        super(LocalizationManager.getInstance().getString("window.coord.title"), true, true, true, true);
        this.model = model;
        model.addPropertyChangeListener(this);

        JPanel panel = new JPanel(new GridLayout(2, 1));
        positionLabel = new JLabel();
        directionLabel = new JLabel();
        panel.add(positionLabel);
        panel.add(directionLabel);
        getContentPane().add(panel);
        setSize(250, 80);
        updateLabels();

        loc.addListener(this::updateLabels);
    }

    private void updateLabels() {
        positionLabel.setText(loc.format("coord.position", model.getRobotX(), model.getRobotY()));
        directionLabel.setText(loc.format("coord.direction", Math.toDegrees(model.getRobotDirection())));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateLabels();
    }
}
