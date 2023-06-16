package com.example.coursework.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {NameAlreadyUsedException.class})
    public ResponseEntity<Object> handleNameAlreadyUsed(final NameAlreadyUsedException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        NameException nameException = new NameException(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(nameException, badRequest);
    }

    @ExceptionHandler(value = {PublisherUpdateNotAllowedException.class})
    public ResponseEntity<Object> handlePublisherUpdateNotAllowed(final PublisherUpdateNotAllowedException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        PublisherException publisherException = new PublisherException(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(publisherException, badRequest);
    }
}
