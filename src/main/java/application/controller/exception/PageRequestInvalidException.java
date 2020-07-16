package application.controller.exception;

public class PageRequestInvalidException extends RuntimeException {
    public PageRequestInvalidException(String message) {
        super(message);
    }
}
