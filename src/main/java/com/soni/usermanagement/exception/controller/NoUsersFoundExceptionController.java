package com.soni.usermanagement.exception.controller;

import com.soni.usermanagement.exception.classes.NoUsersFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NoUsersFoundExceptionController {
    @ExceptionHandler(value = NoUsersFoundException.class)
    public ResponseEntity<Object> exception (NoUsersFoundException exception) {
        return new ResponseEntity<>("No Users Found", HttpStatus.NOT_FOUND);
    }
}