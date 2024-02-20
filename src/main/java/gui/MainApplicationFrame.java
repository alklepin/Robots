package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
 * Что требуется сделать: 1. Метод создания меню перегружен функционалом и трудно читается. Следует
 * разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {

  private final JDesktopPane desktopPane = new JDesktopPane();

  public MainApplicationFrame() {
    //Make the big window be indented 50 pixels from each edge
    //of the screen.
    int inset = 50;
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

    setContentPane(desktopPane);

    LogWindow logWindow = createLogWindow();
    addWindow(logWindow);

    GameWindow gameWindow = new GameWindow();
    gameWindow.setSize(400, 400);
    addWindow(gameWindow);

    setJMenuBar(generateMenuBar());

    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        MainApplicationFrame.this.callCloseDialog();
      }
    });
  }

  protected LogWindow createLogWindow() {
    LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
    logWindow.setLocation(10, 10);
    logWindow.setSize(300, 800);
    setMinimumSize(logWindow.getSize());
    logWindow.pack();
    Logger.debug("Протокол работает");
    return logWindow;
  }

  protected void addWindow(JInternalFrame frame) {
    desktopPane.add(frame);
    frame.setVisible(true);
  }

  private JMenuBar generateMenuBar() {
    return new MainMenuBar();
  }

  private void setLookAndFeel(String className) {
    try {
      UIManager.setLookAndFeel(className);
      SwingUtilities.updateComponentTreeUI(this);
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
             UnsupportedLookAndFeelException e) {
      // just ignore
    }
  }


  private void callCloseDialog() {
    int confirm = JOptionPane.showConfirmDialog(this, "Закрыть приложение?", "Закрыть",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      this.setVisible(false);
      this.dispose();
    }
  }

  public class MainMenuBar extends JMenuBar {

    public MainMenuBar() {
      super();

      this.add(getLookAndFeelMenu());
      this.add(getTestMenu());
      this.add(getCloseMenuItem());
    }

    private static JMenuItem getAddLogMessageMenuItem() {
      JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);

      addLogMessageItem.addActionListener((event) -> {
        Logger.debug("Новая строка");
      });

      return addLogMessageItem;
    }

    private JMenu getLookAndFeelMenu() {
      JMenu lookAndFeelMenu = new JMenu("Режим отображения");
      lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
      lookAndFeelMenu.getAccessibleContext()
          .setAccessibleDescription("Управление режимом отображения приложения");

      lookAndFeelMenu.add(getSystemLookAndFeelMenuItem());
      lookAndFeelMenu.add(getCrossPlatformLookAndFeel());

      return lookAndFeelMenu;
    }

    private JMenuItem getCrossPlatformLookAndFeel() {
      JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);

      crossplatformLookAndFeel.addActionListener((event) -> {
        MainApplicationFrame.this.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        this.invalidate();
      });

      return crossplatformLookAndFeel;
    }

    private JMenuItem getSystemLookAndFeelMenuItem() {
      JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);

      systemLookAndFeel.addActionListener((event) -> {
        MainApplicationFrame.this.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        this.invalidate();
      });

      return systemLookAndFeel;
    }

    private JMenu getTestMenu() {
      JMenu testMenu = new JMenu("Тесты");
      testMenu.setMnemonic(KeyEvent.VK_T);
      testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

      testMenu.add(getAddLogMessageMenuItem());

      return testMenu;
    }

    private JMenuItem getCloseMenuItem() {
      JMenuItem closeMenuItem = new JMenuItem("Закрыть", KeyEvent.VK_C);
      closeMenuItem.getAccessibleContext().setAccessibleDescription("Закрыть приложение");

      closeMenuItem.addActionListener((event) -> {
        MainApplicationFrame.this.callCloseDialog();
      });

      return closeMenuItem;
    }
  }
}
