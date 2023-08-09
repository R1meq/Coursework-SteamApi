package com.example.coursework.controllers;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ControllerTemplate<I, T> {
    List<T> getAll();

    ResponseEntity<T> getById(I id);

    T create(T entity);

    ResponseEntity<T> update(I id, T entity);

    ResponseEntity<T> deleteById(I id);
}
