package com.example.coursework.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class PublisherException {
    private final String massage;
    private final HttpStatus httpStatus;

    public PublisherException(final String massage, final HttpStatus httpStatus) {
        this.massage = massage;
        this.httpStatus = httpStatus;
    }
}