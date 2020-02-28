package com.soni.usermanagement.controller;

import java.time.LocalDateTime;

import com.soni.usermanagement.exception.error.EmailAlreadyExists;
import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.FileAlreadyExists;
import com.soni.usermanagement.exception.error.NoFilesFound;
import com.soni.usermanagement.exception.error.NoUsersFoundException;
import com.soni.usermanagement.exception.error.UserNotFoundException;
import com.soni.usermanagement.exception.success.FileDeleted;
import com.soni.usermanagement.exception.success.FileUpdated;
import com.soni.usermanagement.exception.success.NewFileAdded;
import com.soni.usermanagement.exception.success.NewUserAdded;
import com.soni.usermanagement.exception.success.UserDeleted;
import com.soni.usermanagement.exception.success.UserUpdated;
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

    @ExceptionHandler({UserNotFoundException.class, NoUsersFoundException.class, NoFilesFound.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), HttpStatus.NOT_FOUND.toString(), ex.getMessage()
            );
        
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailNotValidException.class)
    public ResponseEntity<Object> emailNotValidHandler(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE.toString(), ex.getMessage()
            );
        
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({EmailAlreadyExists.class, FileAlreadyExists.class})
    public ResponseEntity<Object> emailAlreadyExistsHandler(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), HttpStatus.CONFLICT.toString(), ex.getMessage()
            );
        
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({NewUserAdded.class, UserUpdated.class, UserDeleted.class, FileDeleted.class, NewFileAdded.class, FileUpdated.class})
    public ResponseEntity<Object> newUserAddedHandler(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), HttpStatus.OK.toString(), ex.getMessage()
            );
        
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.OK);
    }
    
}