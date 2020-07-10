package application.controller;

import application.controller.exception.UserListEmptyException;
import application.model.common.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class UserRestControllerAdvice {

    @ExceptionHandler(UserListEmptyException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponse> handleUserListEmptyException(UserListEmptyException ex) {
        ApiErrorResponse error = new ApiErrorResponse("USER_LIST_EMPTY_ERROR", ex.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus((HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
