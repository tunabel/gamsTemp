package application.controller.exception;

public class UserFieldIncorrectException extends RuntimeException {
    public UserFieldIncorrectException(String message) {
        super(message);
    }
}
