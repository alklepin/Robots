package windowConstructors;

import gui.PositionShowWindow;
import gui.serial.InnerWindowStateContainer;
import serviceLocators.ModelAndControllerLocator;

import javax.swing.*;

public class PositionShowConstructor extends AbstractWindowConstructor{
    public PositionShowConstructor(InnerWindowStateContainer m_windowState) {
        super(m_windowState);
    }
    public PositionShowConstructor(int x, int y,int width,int height){
        super(x,y,width,height);

    }
    @Override
    protected JInternalFrame windowFabricMethod(ModelAndControllerLocator locator) {
        return new PositionShowWindow(locator.getRobotModel());
    }
}
