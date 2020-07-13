package application.controller.exception;

public class EmailWrongFormattingException extends RuntimeException {
    public EmailWrongFormattingException(String message) {
        super(message);
    }
}
