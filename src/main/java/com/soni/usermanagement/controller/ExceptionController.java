package com.soni.usermanagement.controller;

import java.time.LocalDateTime;

import com.soni.usermanagement.exception.error.AppAlreadyExists;
import com.soni.usermanagement.exception.error.AppNotFound;
import com.soni.usermanagement.exception.error.BankAlreadyExists;
import com.soni.usermanagement.exception.error.BankNotFound;
import com.soni.usermanagement.exception.error.EmailAlreadyExists;
import com.soni.usermanagement.exception.error.EmailNotValidException;
import com.soni.usermanagement.exception.error.FileAlreadyExists;
import com.soni.usermanagement.exception.error.FileNotFound;
import com.soni.usermanagement.exception.error.NoAppsFound;
import com.soni.usermanagement.exception.error.NoBanksFound;
import com.soni.usermanagement.exception.error.NoFilesFound;
import com.soni.usermanagement.exception.error.NoUsersFoundException;
import com.soni.usermanagement.exception.error.UserNotFoundException;
import com.soni.usermanagement.exception.success.AppDeleted;
import com.soni.usermanagement.exception.success.AppUpdated;
import com.soni.usermanagement.exception.success.BankDeleted;
import com.soni.usermanagement.exception.success.BankUpdated;
import com.soni.usermanagement.exception.success.FileDeleted;
import com.soni.usermanagement.exception.success.FileUpdated;
import com.soni.usermanagement.exception.success.NewAppAdded;
import com.soni.usermanagement.exception.success.NewBankAdded;
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

    @ExceptionHandler({AppNotFound.class, NoAppsFound.class, BankNotFound.class, NoBanksFound.class, UserNotFoundException.class, NoUsersFoundException.class, NoFilesFound.class, FileNotFound.class})
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

    @ExceptionHandler({AppAlreadyExists.class, BankAlreadyExists.class, EmailAlreadyExists.class, FileAlreadyExists.class})
    public ResponseEntity<Object> emailAlreadyExistsHandler(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), HttpStatus.CONFLICT.toString(), ex.getMessage()
            );
        
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AppUpdated.class, AppDeleted.class, NewAppAdded.class, BankUpdated.class, BankDeleted.class, NewBankAdded.class, NewUserAdded.class, UserUpdated.class, UserDeleted.class, FileDeleted.class, NewFileAdded.class, FileUpdated.class})
    public ResponseEntity<Object> newUserAddedHandler(Exception ex, WebRequest request) {

        ErrorMessage errorMessage = new ErrorMessage(
            LocalDateTime.now(), HttpStatus.OK.toString(), ex.getMessage()
            );
        
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.OK);
    }
    
}