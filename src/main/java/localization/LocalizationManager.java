package localization;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class LocalizationManager {
    private static final String RESOURCES_NAME = "Resources";

    private static LocalizationManager localization_manager_instance = null;

    private static final ResourceBundle.Control CONTROL =
            ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT);

    private final String baseName;
    private final List<ResourceItem> items;
    private ResourceBundle resourceBundle;

    private LocalizationManager(String baseName) {
        this.baseName = baseName;
        items = new LinkedList<>();
        resourceBundle = ResourceBundle.getBundle(baseName, Locale.getDefault(), CONTROL);
    }

    public void bindField(String key, Consumer<String> setter) {
        var item = new ResourceItem(key, setter);
        items.add(item);
        updateItem(item);
    }

    public String getString(String key) {
        return resourceBundle.getString(key);
    }

    public void changeLocale(Locale locale) {
        if (!resourceBundle.getLocale().equals(locale)) {
            resourceBundle = ResourceBundle.getBundle(baseName, locale, CONTROL);
            items.forEach(this::updateItem);
        }
    }

    private void updateItem(ResourceItem item) {
        item.setter.accept(getString(item.key));
    }

    private static class ResourceItem {
        private final String key;
        private final Consumer<String> setter;

        private ResourceItem(String key, Consumer<String> setter) {
            this.key = key;
            this.setter = setter;
        }
    }

    public static LocalizationManager getInstance()
    {
        if (localization_manager_instance == null)
            localization_manager_instance = new LocalizationManager(RESOURCES_NAME);

        return localization_manager_instance;
    }
}

