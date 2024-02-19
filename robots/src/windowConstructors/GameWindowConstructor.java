package windowConstructors;

import gui.GameWindow;
import gui.serial.InnerWindowStateContainer;
import serviceLocators.ModelAndControllerLocator;

import javax.swing.*;

public class GameWindowConstructor extends AbstractWindowConstructor{
    public GameWindowConstructor(InnerWindowStateContainer m_windowState) {
        super(m_windowState);
    }
    public GameWindowConstructor(int x, int y, int width, int height){
        super(x,y,width,height);
        m_windowState=new InnerWindowStateContainer(x,y,width,height);
    }
    @Override
    protected JInternalFrame windowFabricMethod(ModelAndControllerLocator locator) {
        return new GameWindow(locator.getRobotModel(),locator.getTargetPositionController(),locator.getTargetModel());
    }
}
