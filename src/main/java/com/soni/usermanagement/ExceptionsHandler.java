package com.soni.usermanagement;

import java.time.LocalDateTime;
import com.soni.usermanagement.exception.classes.UserNotFoundException;
import com.soni.usermanagement.model.ErrorMessage;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), ex.getMessage(), HttpStatus.NOT_FOUND.value()
            );
        
        return new ResponseEntity<>(
            errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}