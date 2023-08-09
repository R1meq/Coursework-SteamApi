package com.example.coursework.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class NameException {
    private final String massage;
    private final HttpStatus httpStatus;

    public NameException(final String massage, final HttpStatus httpStatus) {
        this.massage = massage;
        this.httpStatus = httpStatus;
    }
}
