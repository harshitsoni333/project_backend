package com.soni.usermanagement.controller;

import java.time.LocalDateTime;

import com.soni.usermanagement.exception.EmailAlreadyExists;
import com.soni.usermanagement.exception.EmailNotValidException;
import com.soni.usermanagement.exception.NoUsersFoundException;
import com.soni.usermanagement.exception.UserNotFoundException;
import com.soni.usermanagement.model.ErrorMessage;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class, NoUsersFoundException.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), ex.getMessage()
            );
        
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailNotValidException.class)
    public ResponseEntity<Object> emailNotValidHandler(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE.value(), HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(), ex.getMessage()
            );
        
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<Object> emailAlreadyExistsHandler(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(), ex.getMessage()
            );
        
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.CONFLICT);
    }
    
}