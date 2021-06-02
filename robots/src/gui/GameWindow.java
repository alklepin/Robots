package gui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements PositionedWindow
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
    
    public Properties getPosition()
    {
    	Properties pr = PositionedWindow.super.getPosition();
		pr.setProperty(getName()+"icon", String.valueOf(isIcon()));
		return pr;
    }
    
    public void restorePosition(Properties pr)
    {
    	PositionedWindow.super.restorePosition(pr);
		 // minimize if necessary
		if (Boolean.parseBoolean(pr.getProperty(getName()+"icon")))
		{
			try {
				setIcon(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
    }
}
