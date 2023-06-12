package gui;

import config.FileSupplier;
import config.PredeterminedPathFileSupplier;
import controllers.RobotUpdateController;
import gui.serial.InnerWindowStateContainer;
import gui.serial.SerializableFrame;
import serviceLocators.ModelAndControllerLocator;
import windowConstructors.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;

public class RobotsProgram {
    private static FileSupplier m_modelsPath = new PredeterminedPathFileSupplier(System.getProperty("user.home").concat("\\modelsConfig.conf"));
    private static FileSupplier m_windowsPath = new PredeterminedPathFileSupplier(System.getProperty("user.home").concat("\\windowsConfig.conf"));
    private static MainApplicationFrame m_frame;
    public static ModelAndControllerLocator m_locator;


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try (var modelsInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(m_modelsPath.getFile().getPath())));
                 var windowsInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(m_windowsPath.getFile().getPath())))) {
                readProgramState(modelsInputStream, windowsInputStream);
            } catch (IOException | ClassNotFoundException e) {
                initProgramState();
            }

            m_frame.pack();
            m_frame.setVisible(true);
            m_frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            m_frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        onExit();
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            });


        });
    }

    private static void initProgramState() {
        m_frame = new MainApplicationFrame();
        m_locator = ModelAndControllerLocator.getDefault();
        InnerWindowStateContainer defaultWindowLayout = new InnerWindowStateContainer(0, 0, 200, 200);
        var updater = new RobotUpdateController(m_locator.getRobotModel());
        ArrayList<WindowConstructor> frameConstructors = new ArrayList<>();
        frameConstructors.add(new GameWindowConstructor(defaultWindowLayout));
        frameConstructors.add(new LogWindowConstructor(defaultWindowLayout));
        frameConstructors.add(new PositionShowConstructor(defaultWindowLayout));

        m_frame = new MainApplicationFrame();
        for (var constructor : frameConstructors) {
            m_frame.addWindow(constructor.construct(m_locator));
        }


    }

    private static void readProgramState(ObjectInputStream modelsReader, ObjectInputStream windowReader) throws IOException, ClassNotFoundException {
        m_frame = new MainApplicationFrame();
        m_locator = ModelAndControllerLocator.getFromConfig(modelsReader);
        var updater = new RobotUpdateController(m_locator.getRobotModel());
        int windowsCount = (Integer) windowReader.readObject();
        for (int i = 0; i < windowsCount; i++) {
            m_frame.addWindow(((WindowConstructor) windowReader.readObject()).construct(m_locator));
        }

    }

    private static void writeProgramState(ObjectOutputStream modelsWriter, ObjectOutputStream windowsWriter) throws IOException {
        m_locator.writeStateToConfig(modelsWriter);
        JInternalFrame[] frames = m_frame.getInternalFrames();
        int frameCount = 0;
        ArrayList<WindowConstructor> constructors = new ArrayList<>();
        for (JInternalFrame frame : frames) {
            if (frame instanceof SerializableFrame) {
                frameCount++;
                constructors.add(((SerializableFrame) frame).getFrameState());
            }
        }
        windowsWriter.writeObject(frameCount);
        for (var constructor : constructors) {
            windowsWriter.writeObject(constructor);
        }
    }

    private static void onExit() throws FileNotFoundException {

        try (var modelsInputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(m_modelsPath.getFile().getPath())));
             var windowsInputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(m_windowsPath.getFile().getPath())))
        ) {

            writeProgramState(modelsInputStream, windowsInputStream);

        } catch (IOException e) {
            throw new RuntimeException(e);

        }


    }
}
