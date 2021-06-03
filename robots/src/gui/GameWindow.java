package gui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.util.Map;
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
    
    @Override
    public Properties getPosition()
    {
    	Properties pr = PositionedWindow.super.getPosition();
		pr.setProperty("icon", String.valueOf(isIcon()));
		return pr;
    }
    
    @Override
    public void restorePosition(Map<String, String> pr)
    {
    	PositionedWindow.super.restorePosition(pr);
		 // minimize if necessary
		if (Boolean.parseBoolean(pr.get("icon")))
		{
			try {
				setIcon(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
    }
}
