package com.initflow.marking.base.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface ReadAndSaveService<T, ID extends Serializable>{

    Optional<T> findOne(ID id);
    boolean exists(ID id);
    List<T> findAll();
    List<T> findAll(Iterable<ID> ids);
    HashSet<T> findAllToSet(Iterable<ID> ids);
    List<T> findAllNoTransactional(Iterable<ID> ids);
    long count();
    List<T> findAll(Sort sort);
    Page<T> findAll(Pageable pageable);
    T save(T obj);
    Iterable<T> saveAll(Iterable<T> objects);
}

