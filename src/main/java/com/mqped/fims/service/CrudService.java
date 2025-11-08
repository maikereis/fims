package com.mqped.fims.service;

import java.util.List;

public interface CrudService<T, ID> {

    T add(T entity);

    List<T> findAll();

    T findById(ID id);

    T update(ID id, T entity);
    void deleteById(ID id);

    boolean existsById(ID id);

    long count();
}