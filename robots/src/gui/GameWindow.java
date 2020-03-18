package gui;

import log.LogChangeListener;
import java.awt.BorderLayout;
import java.util.ResourceBundle;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    private final ResourceBundle localization;
    public GameWindow(ResourceBundle localization)
    {
        super(localization.getString("gameField"), true, true, true, true);
        this.localization = localization;
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);

        //set close button config
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
                if (JOptionPane.showConfirmDialog(e.getInternalFrame(),
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    e.getInternalFrame().getDesktopPane().getDesktopManager().closeFrame(e.getInternalFrame());
                }
            }
        });
        ////

        pack();
    }
}
