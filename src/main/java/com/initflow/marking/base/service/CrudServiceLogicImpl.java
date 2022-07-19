package com.initflow.marking.base.service;

import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.domain.IDObj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class CrudServiceLogicImpl<T extends IDObj<ID>, ID extends Serializable>  implements CrudService<T, ID> {

    protected CrudServiceImpl<T, ID> crudServiceImpl;

    public CrudServiceLogicImpl(CrudServiceImpl<T, ID> crudServiceImpl){
        this.crudServiceImpl = crudServiceImpl;
    }

    @Override
    public <K> T create(K dto, Function<K, T> mapper) {
        return crudServiceImpl.create(dto, mapper);
    }

    @Override
    public <K> T update(ID id, K dto, BiConsumer<K, T> mapper) {
        return crudServiceImpl.update(id, dto, mapper);
    }

    @Override
    public <K> K read(ID id, Function<T, K> mapper) {
        return crudServiceImpl.read(id, mapper);
    }

    @Override
    public <K> Page<K> readList(List<ID> ids, Pageable pageable, Function<T, K> mapper) {
        return crudServiceImpl.readList(ids, pageable, mapper);
    }

    @Override
    public Page<T> findByids(List<ID> ids, Pageable pageable) {
        return crudServiceImpl.findByids(ids, pageable);
    }

    @Override
    public <SR extends SearchRequest<ID>, K> Page<K> searchAndMap(Pageable pageable, SR searchRequest, Function<T, K> mapper) {
        return crudServiceImpl.searchAndMap(pageable, searchRequest, mapper);
    }

    @Override
    public <SR extends SearchRequest<ID>> Page<T> findAll(Pageable pageable, SR searchRequest) {
        return crudServiceImpl.findAll(pageable, searchRequest);
    }

    @Override
    public Optional<T> findOne(ID id) {
        return crudServiceImpl.findOne(id);
    }

    @Override
    public boolean exists(ID id) {
        return crudServiceImpl.exists(id);
    }

    @Override
    public List<T> findAll() {
        return crudServiceImpl.findAll();
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        return crudServiceImpl.findAll(ids);
    }

    @Override
    public HashSet<T> findAllToSet(Iterable<ID> ids) {
        return crudServiceImpl.findAllToSet(ids);
    }

    @Override
    public List<T> findAllNoTransactional(Iterable<ID> ids) {
        return crudServiceImpl.findAllNoTransactional(ids);
    }

    @Override
    public long count() {
        return crudServiceImpl.count();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return crudServiceImpl.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return crudServiceImpl.findAll(pageable);
    }

    @Override
    public T save(T obj) {
        return crudServiceImpl.save(obj);
    }

    @Override
    public Iterable<T> saveAll(Iterable<T> objects) {
        return crudServiceImpl.saveAll(objects);
    }

    public <SR extends SearchRequest<ID>> T findFirst(SR sr) {
        return crudServiceImpl.findFirst(sr);
    }

    @Override
    public <SR extends SearchRequest<ID>> long count(SR searchRequest) {
        return crudServiceImpl.count(searchRequest);
    }
}
