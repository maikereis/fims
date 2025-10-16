package com.mqped.fims.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {

    T add(T entity);

    List<T> findAll();

    Optional<T> findById(ID id);

    Optional<T> update(ID id, T entity);

    boolean deleteById(ID id);

    boolean existsById(ID id);

    long count();
}