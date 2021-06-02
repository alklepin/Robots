package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
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
public class MainApplicationFrame extends JFrame implements PositionedWindow
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private WindowsPosition position;
    private ArrayList<PositionedWindow> windows = new ArrayList<PositionedWindow>();
    public MainApplicationFrame() {
    	windows.add(this);
    	
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
        windows.add(logWindow);
        
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        gameWindow.setName("GameWindow");
        addWindow(gameWindow);
        windows.add(gameWindow);
        
        setJMenuBar(generateMenuBar());
        
        position = new WindowsPosition();
        Properties pr = position.readPositionFromFile(); // load coordinates and size from xml
        position.restorePosition(pr, windows);
        
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
    
    public Properties getPosition()
    {
    	return PositionedWindow.super.getPosition();
    }
    public void restorePosition(Properties pr)
    {
    	PositionedWindow.super.restorePosition(pr);
    }
    
    // exit confirmation
    protected void exitDialog() 
    {
    	int answer = JOptionPane.showConfirmDialog(this, "Закрыть программу?", 
	               "Подтверждение выхода", JOptionPane.YES_NO_OPTION);
    	if (answer == JOptionPane.YES_OPTION)
    	{
    		Properties pr = position.createProperties(windows);
    		position.savePositionToFile(pr); // save coordinates and size to xml
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
