package application.controller.exception;

public class BadConnectionException extends RuntimeException {
    public BadConnectionException(String message) {
        super(message);
    }
}
