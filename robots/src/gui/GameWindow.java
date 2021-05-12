package gui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
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
    
    public String getPosition()
    {
		String position = String.format( "%d,%d,%d,%d,%b", getX(), getY(), 
									getWidth(), getHeight(), isIcon() );
		return position;
    }
    
    public void restorePosition(String position)
    {
    	// set coordinates and size
    	String pos[] = position.split(",");
		setBounds(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]),
					Integer.parseInt(pos[2]), Integer.parseInt(pos[3]));
		
		 // minimize if necessary
		if (Boolean.parseBoolean(pos[4]))
		{
			try {
				setIcon(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
    }
}
