package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.*;

import javax.swing.*;

import log.Logger;

import static java.lang.Integer.parseInt;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        CreateInternalWindows();
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ReadPreviousSettings();

    }

    private void ReadPreviousSettings() {
        try (FileReader fr = new FileReader("text.txt");) {
            var t = "";
            var c = 0;
            while ((c = fr.read()) != -1) {

                t += (char) c;
            }
            SetPreviousParameters(t);
        } catch (IOException ex) {

            System.out.println("не считали");
        }
    }

    private void CreateInternalWindows() {
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);
        var parameters = new Parameters();

        CoordinatesWindow coordinatesWindow = createCoordinatesWindow(parameters);
        coordinatesWindow.setSize(300, 150);
        coordinatesWindow.setMinimumSize(coordinatesWindow.getSize());
        addWindow(coordinatesWindow);

        parameters.addObserver(coordinatesWindow);

        GameWindow gameWindow = new GameWindow(parameters);
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);
    }

    private CoordinatesWindow createCoordinatesWindow(Parameters parameters) {
        var coordinatesWindow = new CoordinatesWindow(parameters);
        coordinatesWindow.setLocation(400, 400);
        coordinatesWindow.pack();
        return coordinatesWindow;
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
        JMenuBar menuBar = new JMenuBar();


        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }


        JMenu file = new JMenu("Файл");
        file.setMnemonic(KeyEvent.VK_V);
        file.getAccessibleContext().setAccessibleDescription(
                "Управление приложением");
        {
            JMenuItem closeMenu = new JMenuItem("Закрыть приложение", KeyEvent.VK_S);
            closeMenu.addActionListener((event) -> {
                int res = JOptionPane.showOptionDialog(null, "Закрыть приложение?", null, JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Да", "Нет"}, null);
                if (res == 0) {
                    SaveParameters();
                    System.exit(0);
                }
            });
            file.add(closeMenu);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }

        menuBar.add(file);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private void SaveParameters() {
        //var temp ="";
        try (FileWriter writer = new FileWriter("text.txt", false)) {

            var t = this.desktopPane.getAllFrames();

            for (int i = 0; i < t.length; i++) {
                var location = t[i].getLocation();
                var size = t[i].getSize();
                var isShowing = t[i].isShowing();

                t[i].setResizable(true);
                var temp = t[i].getTitle() + "\n" + location.x + " " + location.y + "\n" + size.width + " " + size.height + "\n" + (isShowing ? "T" : "F") + "\n";
//                writer.write(location.x+" "+ location.y + "\n");
//                writer.write(size.width+" "+ size.height + "\n");
//                writer.write(isShowing?"T":"F");
//                writer.write("\n");
                writer.write(temp);
            }
            writer.flush();
        } catch (IOException ex) {

            System.out.println("не сохранили");
        }

        //System.out.println(temp);
    }

    private void SetPreviousParameters(String text) {
        if (text.length() == 0)
            return;
        var t = this.desktopPane.getAllFrames();
        var arr = text.split("\n");
        var p = 0;
        var counter = 0;
        while (counter < t.length) {
            for (int i = 0; i < t.length; i++) {
                var name = arr[0 + p + counter];
                if (t[i].getTitle().equals(name)) {
                    var current = t[i];
                    var location = arr[counter + p + 1].split(" ");
                    current.setLocation(parseInt(location[0]), parseInt(location[1]));
                    var size = arr[counter + 2 + p].split(" ");
                    current.setSize(parseInt(size[0]), parseInt(size[1]));
                    var showing = arr[counter + 3 + p];
                    current.setBounds(parseInt(location[0]), parseInt(location[1]), parseInt(size[0]), parseInt(size[1]));
                    if (showing.equals("F")) {
                        try {
                            current.setIcon(true);
                        } catch (PropertyVetoException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    p += 3;
                    counter += 1;
                    break;
                }
            }
        }
//        for (int i = 0; i<t.length; i++){
//            var location = arr[i+p].split(" ");
//            t[i].setLocation(parseInt(location[0]), parseInt(location[1]));
//            var size = arr[i+1+p].split(" ");
//            t[i].setSize(parseInt(size[0]), parseInt(size[1]));
//            var showing = arr[i+2+p];
//            t[i].setBounds(parseInt(location[0]), parseInt(location[1]),parseInt(size[0]), parseInt(size[1]));
//            if(showing.equals("F")) {
//                try {
//                    t[i].setIcon(true);
//                } catch (PropertyVetoException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            p+=2;
//        }
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }
}
