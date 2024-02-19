package windowConstructors;

import serviceLocators.ModelAndControllerLocator;

import javax.swing.*;

public interface WindowConstructor {
    JInternalFrame construct(ModelAndControllerLocator locator);
}
