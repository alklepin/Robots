package windowConstructors;

import gui.serial.InnerWindowStateContainer;
import serviceLocators.ModelAndControllerLocator;

import javax.swing.*;
import java.io.Serializable;


public abstract class AbstractWindowConstructor implements WindowConstructor, Serializable {
    protected InnerWindowStateContainer m_windowState;

    protected AbstractWindowConstructor(InnerWindowStateContainer m_windowState) {
        this.m_windowState = m_windowState;
    }
    public AbstractWindowConstructor(int x, int y,int width,int height){
        m_windowState=new InnerWindowStateContainer(x,y,width,height);
    }
    protected abstract JInternalFrame windowFabricMethod(ModelAndControllerLocator locator);
    public JInternalFrame construct(ModelAndControllerLocator locator){
        var window= windowFabricMethod(locator);
        m_windowState.applyState(window);
        return window;
    }
}
