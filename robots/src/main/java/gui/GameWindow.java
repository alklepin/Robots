package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements Recoverable
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
    public void saveState(int width, int height) {
        DictState formDictState = new DictState();
        formDictState.addState("width", String.valueOf(width));
        formDictState.addState("height", String.valueOf(height));
        WindowStateDict uniteDict = new WindowStateDict();
        uniteDict.unite("model", formDictState);
        uniteDict.writeStateToFile();
    }

    @Override
    public DictState getRecoveryState() {
        WindowStateDict uniteDict = new WindowStateDict();
        uniteDict.readStateFromFile();
        return uniteDict.getDictStateByName("model");
    }
}
