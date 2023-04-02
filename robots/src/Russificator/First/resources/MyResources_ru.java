package Russificator.First.resources;

import java.util.ListResourceBundle;

public class MyResources_ru extends ListResourceBundle {

  @Override
  protected Object[][] getContents() {

    return resources;
  }

  private final Object[][] resources = {

      {"name", "Имя"},
      {"lang_menu", "Меню"},
      {"lang_ru", "Русский"},
      {"lang_hu", "Венгерский"},
      {"description", "Какой-то смысл..."}
  };
}
