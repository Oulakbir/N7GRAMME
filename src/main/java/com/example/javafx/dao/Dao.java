package com.example.javafx.dao;

import java.util.List;

public interface Dao<T, U>{
    void save(T o);
    void removeById(U id);
    T getById(U id);
    List<T> getAll();
    void update(T o);
}
