package com.soni.usermanagement.controller;

import java.time.LocalDateTime;

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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), HttpStatus.NOT_FOUND.toString(), ex.getMessage()
            );
        
        return new ResponseEntity<>(
            errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}