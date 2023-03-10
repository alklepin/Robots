package org.iffomko.savers;

public class SaversException extends RuntimeException {
    private static class ErrorCode {
        private final int errorCode;
        private final String errorMessage;


        public ErrorCode(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }

        public String getMessage() {
            return errorMessage;
        }

        public int getCode() {
            return errorCode;
        }
    }

    private ErrorCode currentError;

    public static final int UNKNOWN_ERROR_EXCEPTION_CODE = 0;

    public static final int NOT_SUPPORTED_METHOD_EXCEPTION_CODE = 1;
    public static final int ILLEGAL_STATE_EXCEPTION_CODE = 2;
    public static final int ILLEGAL_PREFIX_EXCEPTION_CODE = 3;
    public static final int PREFIX_NOT_EXIST_EXCEPTION_CODE = 4;

    public static final ErrorCode NOT_SUPPORTED_METHOD_EXCEPTION = new ErrorCode(
            NOT_SUPPORTED_METHOD_EXCEPTION_CODE,
            "Этот метод не поддерживается"
    );

    public static final ErrorCode ILLEGAL_STATE_EXCEPTION = new ErrorCode(
            ILLEGAL_STATE_EXCEPTION_CODE,
            "Используется некорректное состояние поля state"
    );

    public static final ErrorCode UNKNOWN_ERROR_EXCEPTION = new ErrorCode(
            UNKNOWN_ERROR_EXCEPTION_CODE,
            "Произошла неизвестная ошибка"
    );

    public static final ErrorCode ILLEGAL_PREFIX_EXCEPTION = new ErrorCode(
        ILLEGAL_PREFIX_EXCEPTION_CODE,
            "Ввели некорректное значение префикса"
    );
    public static final ErrorCode PREFIX_NOT_EXIST_EXCEPTION = new ErrorCode(
            PREFIX_NOT_EXIST_EXCEPTION_CODE,
            "В вашем ключе нет префикса или он пустой"
    );

    public SaversException(int errorCode) {
        switch (errorCode) {
            case NOT_SUPPORTED_METHOD_EXCEPTION_CODE: {
                currentError = NOT_SUPPORTED_METHOD_EXCEPTION;
                break;
            }
            case ILLEGAL_STATE_EXCEPTION_CODE: {
                currentError = ILLEGAL_STATE_EXCEPTION;
                break;
            }
            case ILLEGAL_PREFIX_EXCEPTION_CODE: {
                currentError = ILLEGAL_PREFIX_EXCEPTION;
                break;
            }
            case PREFIX_NOT_EXIST_EXCEPTION_CODE: {
                currentError = PREFIX_NOT_EXIST_EXCEPTION;
                break;
            }
            default: {
                currentError = UNKNOWN_ERROR_EXCEPTION;
                break;
            }
        }
    }

    public String getMessage() {
        return currentError.getMessage();
    }
}
