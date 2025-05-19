package org.example.repository;

import java.util.List;

public interface Repository<T, ID> {
    void create(T entity);
    T findById(ID id);
    List<T> findByName(String pattern);
}
