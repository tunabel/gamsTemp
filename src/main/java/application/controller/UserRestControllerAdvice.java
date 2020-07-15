package application.controller;

import application.controller.exception.*;
import application.model.responseData.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class UserRestControllerAdvice {

    @ExceptionHandler(UserListEmptyException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<ApiErrorResponse> handleUserListEmptyException(UserListEmptyException ex) {
        ApiErrorResponse error = new ApiErrorResponse("USER_LIST_EMPTY_ERROR", ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.NO_CONTENT.value());
        return new ResponseEntity<>(error, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        ApiErrorResponse error = new ApiErrorResponse("USER_NOT_FOUND_ERROR", ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailExistedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleEmailExistedException(EmailExistedException ex) {
        ApiErrorResponse error = new ApiErrorResponse("SUBMITTED_EMAIL_EXISTED", ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailWrongFormattingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleEmailWrongFormattingException(EmailWrongFormattingException ex) {
        ApiErrorResponse error = new ApiErrorResponse("SUBMITTED_EMAIL_WITH_WRONG_FORMATTING", ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadConnectionException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponse> handleBadConnectionException(BadConnectionException ex) {
        ApiErrorResponse error = new ApiErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserFieldIncorrectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleUserFieldIncorrectException(UserFieldIncorrectException ex) {
        ApiErrorResponse error = new ApiErrorResponse("USER_FIELD_PARAMETER_ERROR", ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    public ResponseEntity<ApiErrorResponse> handleOtherException(Exception ex) {
//        ApiErrorResponse error = new ApiErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage());
//        error.setTimestamp(LocalDateTime.now());
//        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
