package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JInternalFrame.JDesktopIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
	
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final String filename = System.getProperty("user.home") +
			System.getProperty("file.separator") + "robot_settings.xml";
    public MainApplicationFrame() {
    	
    	// russian text on buttons
    	UIManager.put("OptionPane.yesButtonText", "Да" );
    	UIManager.put("OptionPane.noButtonText", "Нет");
    	
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        LogWindow logWindow = createLogWindow();
        logWindow.setName("LogWindow");
        addWindow(logWindow);
        
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        gameWindow.setName("GameWindow");
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());
        
        restorePosition(); // load coordinates and size from xml
        
        // exit button listener
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                exitDialog();
            }
        });
    }
    
    // save windows settings
    protected void savePosition() 
    {
    	Properties pr = new Properties();
    	
    	// main window 
    	String position = String.format( "%d,%d,%d,%d", 
    			this.getX(), this.getY(), 
    			this.getWidth(), this.getHeight() );
        pr.setProperty( this.getName(), position );
     
        // internal frames
    	for (Component c : desktopPane.getComponents() )
    	{
    		if (c instanceof JInternalFrame || c instanceof JInternalFrame.JDesktopIcon)
    		{
    			int icon = 0; // set if frame is minimized
    			if (c instanceof JDesktopIcon) 
    			{
    				c = ((JDesktopIcon) c).getInternalFrame();
    				icon = 1;
    			}
    			position = String.format( "%d,%d,%d,%d,%d", c.getX(), c.getY(), 
    										c.getWidth(), c.getHeight(), icon );
    			pr.setProperty( c.getName(), position );
    		}
    	}
    	
    	// save to file
    	try {
			OutputStream os = new FileOutputStream(filename);
			pr.storeToXML(os, null);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    // restore windows settings
    protected void restorePosition()
    {
    	// try open file and load properties
    	InputStream is;
    	Properties pr = new Properties();
    	try {
			is = new FileInputStream(filename);
			pr.loadFromXML(is);
			is.close();
		} catch (FileNotFoundException e) {
			// do nothing in case of exceptions
			return;
		} catch (InvalidPropertiesFormatException e) {
			return;
		} catch (IOException e) {
			return;
		}
	
    	// main window
		String[] pos = pr.getProperty(this.getName()).split(",");
		this.setBounds(	Integer.parseInt(pos[0]), Integer.parseInt(pos[1]),
						Integer.parseInt(pos[2]), Integer.parseInt(pos[3]));
		
		// internal frames
		for (Component c : desktopPane.getComponents() )
    	{
    		if (c instanceof JInternalFrame && c.getName() != null 
    				&& pr.containsKey(c.getName()))
    		{
    			// set coordinates and size
    			pos = pr.getProperty(c.getName()).split(",");
    			c.setBounds(Integer.parseInt(pos[0]), Integer.parseInt(pos[1]),
    						Integer.parseInt(pos[2]), Integer.parseInt(pos[3]));
    			
    			 // minimize if necessary
    			if (Integer.parseInt(pos[4]) == 1)
    			{
    				try {
						((JInternalFrame) c).setIcon(true);
					} catch (PropertyVetoException e) {
						e.printStackTrace();
					}
    			}
    		}
    	}
    }
    
    // exit confirmation
    protected void exitDialog() 
    {
    	int answer = JOptionPane.showConfirmDialog(this, "Закрыть программу?", 
	               "Подтверждение выхода", JOptionPane.YES_NO_OPTION);
    	if (answer == JOptionPane.YES_OPTION)
    	{
    		savePosition(); // save coordinates and size to xml
    		System.exit(0);
    	}
    }
    
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
    
    
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu mainMenu = createMenu(menuBar, "RobotsProgram", "Основное меню", KeyEvent.VK_M);
        JMenuItem exitItem = createMenuItem(mainMenu, "Выход", KeyEvent.VK_Q);
        exitItem.addActionListener((event) -> {
        	exitDialog();
        });
        
        JMenu lookAndFeelMenu = createMenu(menuBar, "Режим отображения", 
        		"Управление режимом отображения приложения", KeyEvent.VK_V);
        JMenuItem systemLookAndFeel = createMenuItem(lookAndFeelMenu, 
        		"Системная схема", KeyEvent.VK_S);
        addLookAndFeelListener(systemLookAndFeel, UIManager.getSystemLookAndFeelClassName());
        JMenuItem crossplatformLookAndFeel = createMenuItem(lookAndFeelMenu, 
        		"Универсальная схема", KeyEvent.VK_S);
        addLookAndFeelListener(crossplatformLookAndFeel, UIManager.getCrossPlatformLookAndFeelClassName());

        JMenu testMenu = createMenu(menuBar, "Тесты", "Тестовые команды", KeyEvent.VK_T);
        JMenuItem addLogMessageItem = createMenuItem(testMenu, "Сообщение в лог", KeyEvent.VK_S);
        addLogMessageItem.addActionListener((event) -> {
        	Logger.debug("Новая строка");
        });
        
        return menuBar;
    }
    
    private JMenu createMenu(JMenuBar menuBar, String name, String desc, int key) 
    {
    	JMenu menu = new JMenu(name);
        menu.setMnemonic(key);
        menu.getAccessibleContext().setAccessibleDescription(desc);
        menuBar.add(menu);
        return menu;
    }
    
    private JMenuItem createMenuItem(JMenu menu, String name, int key)
    {
    	JMenuItem item = new JMenuItem(name, key);
        menu.add(item);
        return item;
    }
    
    private void addLookAndFeelListener(JMenuItem item, String name)
    {
    	item.addActionListener((event) -> {
        	setLookAndFeel(name);
        	this.invalidate();
        });
    }
    
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
