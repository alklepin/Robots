package Russificator.First.resources;

import java.util.ListResourceBundle;

public class MyResources_hu extends ListResourceBundle {

  @Override
  protected Object[][] getContents() {

    return resources;
  }

  private final Object[][] resources = {

      {"name", "Magyarország"},
      {"lang_menu", "Nyelv"},
      {"lang_ru", "Orosz"},
      {"lang_hu", "Magyar"},
      {"description", "Magyarország közép-európai ország "
          + "a Kárpát-medencében."}
  };
}

