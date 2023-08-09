package com.example.coursework.services;

import java.util.List;

public interface ServiceTemplate<I, T> {
    List<T> getAll();

    T getById(I id);

    T create(T entity);

    T update(I id, T entity);

    void deleteById(I id);

}
