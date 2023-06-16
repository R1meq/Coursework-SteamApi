package com.example.coursework.exceptions;

public class NameAlreadyUsedException  extends RuntimeException {

    public NameAlreadyUsedException(String massage) {
        super(massage);
    }
}
