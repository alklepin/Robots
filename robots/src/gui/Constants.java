package gui;

public interface Constants {

    class MainApplicationFrameConstants {

        public static final int SCREEN_OFFSET = 50;
        public static final int INITIAL_GAME_WINDOW_HEIGHT = 400;
        public static final int INITIAL_GAME_WINDOW_WIDTH = 400;
        public static final int LOG_WINDOW_INITIAL_LOCATION_X = 10;
        public static final int LOG_WINDOW_INITIAL_LOCATION_Y = 10;
        public static final int LOG_WINDOW_INITIAL_WIDTH = 300;
        public static final int LOG_WINDOW_INITIAL_HEIGHT = 800;
        public static final String LOGGER_INITIAL_MESSAGE = "Протокол работает";
        public static final String EXIT_BUTTON_TEXT = "Выход";
        public static final String YES_BUTTON_TEXT_CONSTANT = "OptionPane.yesButtonText";
        public static final String YES_BUTTON_TEXT = "Да";
        public static final String NO_BUTTON_TEXT_CONSTANT = "OptionPane.noButtonText";
        public static final String NO_BUTTON_TEXT = "Нет";
        public static final String EXIT_CONFIRM_DIALOG_TEXT = "Вы уверены?";
        public static final String EXIT_CONFIRM_DIALOG_TITLE = "ВЫХОД";
        public static final String TEST_MENU_TEXT = "Тесты";
        public static final String TEST_LOG_OPTION_TEXT = "Сообщение в лог";
        public static final String TEST_LOG_TEXT = "Новая строка";
        public static final String STYLE_MENU_TEXT = "Режим отображения";
        public static final String CROSS_PLATFORM_STYLE_TEXT = "Универсальная схема";
        public static final String SYSTEM_STYLE_TEXT = "Системная схема";
    }

    class RobotsProgramConstants {

        public static final String NIMBUS_MENU_STYLE = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        public static final String METAL_MENU_STYLE = "javax.swing.plaf.metal.MetalLookAndFeel";
    }

    class LogWindowConstants {

        public static final String INITIAL_LOG_MESSAGE = "Протокол работы";
        public static final int LOG_TEXT_AREA_WIDTH = 200;
        public static final int LOG_TEXT_AREA_HEIGHT = 500;
    }

    class GameWindowConstants {

        public static final String GAME_WINDOW_TITLE = "Игровое поле";
    }
}
