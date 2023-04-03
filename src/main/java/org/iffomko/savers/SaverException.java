package org.iffomko.savers;

public class SaverException extends Exception {
    private static class ErrorCode {
        private final String errorMessage;


        public ErrorCode(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getMessage() {
            return errorMessage;
        }
    }

    private final ErrorCode currentError;

    public SaverException(String errorMessage) {
        currentError = new ErrorCode(errorMessage);
    }

    public String getMessage() {
        return currentError.getMessage();
    }
}
