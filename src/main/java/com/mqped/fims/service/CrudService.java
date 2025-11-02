package com.mqped.fims.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T, ID> {

    T add(T entity);

    List<T> findAll();

    Optional<T> findById(ID id);

    Optional<T> update(ID id, T entity);

    void deleteById(ID id);

    boolean existsById(ID id);

    long count();
}