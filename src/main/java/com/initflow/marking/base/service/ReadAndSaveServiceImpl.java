package com.initflow.marking.base.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public abstract class ReadAndSaveServiceImpl<T, ID extends Serializable> implements ReadAndSaveService<T, ID>{

    protected abstract PagingAndSortingRepository<T, ID> getRepository();

    public Optional<T> findOne(ID id){
        return getRepository().findById(id);
    }

    public boolean exists(ID id){
        return getRepository().existsById(id);
    }

    public List<T> findAll(){
        return Lists.newArrayList(getRepository().findAll());
    }

    public List<T> findAll(Iterable<ID> ids){
        return Lists.newArrayList(getRepository().findAllById(ids));
    }

    public HashSet<T> findAllToSet(Iterable<ID> ids){
        return Sets.newHashSet(getRepository().findAllById(ids));
    }

    public List<T> findAllNoTransactional(Iterable<ID> ids){
        return Lists.newArrayList(getRepository().findAllById(ids));
    }

    public long count(){
        return getRepository().count();
    }

    public List<T> findAll(Sort sort){
        return Lists.newArrayList(getRepository().findAll(sort));
    }

    public Page<T> findAll(Pageable pageable){
        return getRepository().findAll(pageable);
    }

    public T save(T obj){
        return getRepository().save(obj);
    }

    public Iterable<T> saveAll(Iterable<T> objects){
        return getRepository().saveAll(objects);
    }
}
