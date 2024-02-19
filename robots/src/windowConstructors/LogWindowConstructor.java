package windowConstructors;

import gui.LogWindow;
import gui.serial.InnerWindowStateContainer;
import serviceLocators.ModelAndControllerLocator;

import javax.swing.*;

public class LogWindowConstructor extends AbstractWindowConstructor{
    public LogWindowConstructor(InnerWindowStateContainer m_windowState) {
        super(m_windowState);
    }
    public LogWindowConstructor(int x, int y,int width,int height){
        super(x,y,width,height);
        m_windowState=new InnerWindowStateContainer(x,y,width,height);
    }
    @Override
    protected JInternalFrame windowFabricMethod(ModelAndControllerLocator locator) {
        return new LogWindow(locator.getLogSource());
    }
}
