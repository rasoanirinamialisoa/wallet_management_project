package org.example.repository;

import org.example.model.Currency;

import java.util.List;
public interface CrudOperations<T> {
    List<T> findAll();
    List<T> saveAll(List<T> toSave);
    T save(T toSave);

    Currency findById(int accountId);
}

