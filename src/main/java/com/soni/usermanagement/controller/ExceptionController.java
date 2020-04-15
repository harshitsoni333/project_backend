package com.soni.usermanagement.controller;

import java.time.LocalDateTime;

import com.soni.usermanagement.dto.ResponseMessage;
import com.soni.usermanagement.exception.EmailNotValidException;
import com.soni.usermanagement.exception.EntryAlreadyExists;
import com.soni.usermanagement.exception.EntryNotFound;
import com.soni.usermanagement.exception.InvalidEntry;
import com.soni.usermanagement.exception.PasswordNotValid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntryNotFound.class)
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request) {

        ResponseMessage responseMessage = new ResponseMessage(
            LocalDateTime.now(), HttpStatus.NOT_FOUND.toString(), ex.getMessage()
            );
        
        return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({PasswordNotValid.class, EmailNotValidException.class, InvalidEntry.class})
    public ResponseEntity<Object> invalidEntryHandler(Exception ex, WebRequest request) {

        ResponseMessage responseMessage = new ResponseMessage(
            LocalDateTime.now(), HttpStatus.NOT_ACCEPTABLE.toString(), ex.getMessage()
            );
        
        return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(EntryAlreadyExists.class)
    public ResponseEntity<Object> emailAlreadyExistsHandler(Exception ex, WebRequest request) {

        ResponseMessage responseMessage = new ResponseMessage(
            LocalDateTime.now(), HttpStatus.CONFLICT.toString(), ex.getMessage()
            );
        
        return new ResponseEntity<>(responseMessage, new HttpHeaders(), HttpStatus.CONFLICT);
    }
    
}