package gui;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.util.ResourceBundle;

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


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        // Confirm close window
        addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
                Object[] options = { localization.getString("closeWindowYes"), localization.getString("closeWindowNo") };
                if (JOptionPane.showOptionDialog(e.getInternalFrame(),
                        localization.getString("closeWindowQuestion"), localization.getString("closeWindowTitle"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null, options, null) == 0)
                {
                    e.getInternalFrame().getDesktopPane().getDesktopManager().closeFrame(e.getInternalFrame());
                }
            }
        });

        pack();
    }
}
