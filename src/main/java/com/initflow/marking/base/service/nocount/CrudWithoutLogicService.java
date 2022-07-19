package com.initflow.marking.base.service.nocount;

import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.domain.IDObj;
import com.initflow.marking.base.service.ReadAndSaveService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

public abstract class CrudWithoutLogicService<T extends IDObj<Long>>
        implements ReadAndSaveService<T, Long>, ICrudServiceImplWithoutCount<T, Long> {

    protected abstract CrudServiceImplWithoutCount<T> getService();

    @Override
    public boolean exists(Long id) {
        return getService().exists(id);
    }

    @Override
    public List<T> findAll() {
        return getService().findAll();
    }

    @Override
    public List<T> findAll(Iterable<Long> ids) {
        return getService().findAll(ids);
    }

    @Override
    public HashSet<T> findAllToSet(Iterable<Long> ids) {
        return getService().findAllToSet(ids);
    }

    @Override
    public List<T> findAllNoTransactional(Iterable<Long> ids) {
        return getService().findAllNoTransactional(ids);
    }

    @Override
    public long count() {
        return getService().count();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return getService().findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return getService().findAll(pageable);
    }

    @Override
    public <K> Page<K> readList(List<Long> ids, Pageable pageable, Function<T, K> mapper) {
        return getService().readList(ids, pageable, mapper);
    }

    @Override
    public Page<T> findByids(List<Long> ids, Pageable pageable) {
        return getService().findByids(ids, pageable);
    }

    @Override
    public <SR extends SearchRequest<Long>, K> Page<K> searchAndMap(Pageable pageable, SR searchRequest, Function<T, K> mapper) {
        return getService().searchAndMap(pageable, searchRequest, mapper);
    }

    @Override
    public <SR extends SearchRequest<Long>> Page<T> findAll(Pageable pageable, SR searchRequest) {
        return getService().findAll(pageable, searchRequest);
    }

    @Override
    public <SR extends SearchRequest<Long>> T findFirst(SR sr) {
        return getService().findFirst(sr);
    }


    @Override
    public <SR extends SearchRequest<Long>> Page<T> findAllWithoutCount(Pageable pageable, SR searchRequest) {
        return getService().findAllWithoutCount(pageable, searchRequest);
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        return getService().findAll(predicate, pageable);
    }

    @Override
    public <SR extends SearchRequest<Long>, K> Page<K> searchWithoutCountAndMap(Pageable pageable, SR searchRequest, Function<T, K> mapper, int limit, int offset){
        return getService().searchWithoutCountAndMap(pageable, searchRequest, mapper, limit, offset);
    }

    @Override
    public <SR extends SearchRequest<Long>> long count(SR searchRequest) {
        return getService().count(searchRequest);
    }

}
