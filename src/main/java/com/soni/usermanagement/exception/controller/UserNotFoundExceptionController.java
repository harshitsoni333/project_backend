package com.soni.usermanagement.exception.controller;

import com.soni.usermanagement.exception.classes.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserNotFoundExceptionController {
    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> exception (UserNotFoundException exception) {
        return new ResponseEntity<>("User does not exist", HttpStatus.NOT_FOUND);
    }
}