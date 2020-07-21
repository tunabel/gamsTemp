package application.controller;

import application.controller.exception.*;
import application.model.responsedto.ApiErrorResponseDto;
import application.model.responsedto.Violation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class UserRestControllerAdvice {

    @ExceptionHandler(BadConnectionException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiErrorResponseDto> handleBadConnectionException(BadConnectionException ex) {
        ApiErrorResponseDto error = new ApiErrorResponseDto("INTERNAL_SERVER_ERROR", ex.getMessage());
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserListEmptyException.class)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<ApiErrorResponseDto> handleUserListEmptyException(UserListEmptyException ex) {
        ApiErrorResponseDto error = new ApiErrorResponseDto("USER_LIST_EMPTY", ex.getMessage());
        error.setStatus(HttpStatus.NO_CONTENT.value());
        return new ResponseEntity<>(error, HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(UserFieldIncorrectException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponseDto> handleUserFieldIncorrectException(UserFieldIncorrectException ex) {
        ApiErrorResponseDto error = new ApiErrorResponseDto("USER_FIELD_INCORRECT", ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiErrorResponseDto> handleItemNotFoundException(ItemNotFoundException ex) {
        ApiErrorResponseDto error = new ApiErrorResponseDto("ID_NOT_FOUND", ex.getMessage());
        error.setStatus(HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailExistedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponseDto> handleEmailExistedException(EmailExistedException ex) {
        ApiErrorResponseDto error = new ApiErrorResponseDto("SUBMITTED_EMAIL_EXISTED", ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponseDto> handleRoleNotFoundException(RoleNotFoundException ex) {
        ApiErrorResponseDto error = new ApiErrorResponseDto("SUBMITTED_ROLE_NOT_FOUND", ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponseDto> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ApiErrorResponseDto error = new ApiErrorResponseDto("SUBMITTED_EMAIL_NOT_FOUND", ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PageRequestInvalidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponseDto> handlePageRequestInvalidException(PageRequestInvalidException ex) {
        ApiErrorResponseDto error = new ApiErrorResponseDto("REQUEST_PAGE_INVALID", ex.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ApiErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiErrorResponseDto error = new ApiErrorResponseDto("UNMET_VALIDATION", "Validation error");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        for (ConstraintViolation<?> violation : ex.getConstraintViolations() ) {
            error.getErrors().add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage())
            );
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //Reformatting validation errors from request bodies
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ApiErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiErrorResponseDto error = new ApiErrorResponseDto("INCORRECT_INPUT", "Input fields not correct");
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            error.getErrors().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage())
            );
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



}
