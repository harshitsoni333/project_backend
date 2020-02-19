package com.soni.usermanagement.exception.controller;

import com.soni.usermanagement.exception.classes.EmailNotValidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EmailNotValidExceptionController {
    @ExceptionHandler(value = EmailNotValidException.class)
    public ResponseEntity<Object> exception (EmailNotValidException exception) {
        return new ResponseEntity<>("Email not valid", HttpStatus.NOT_ACCEPTABLE);
    }
}
