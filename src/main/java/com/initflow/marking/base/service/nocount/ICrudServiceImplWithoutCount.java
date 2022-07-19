package com.initflow.marking.base.service.nocount;

import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.domain.IDObj;
import com.initflow.marking.base.service.CrudService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.function.Function;

public interface ICrudServiceImplWithoutCount<T extends IDObj<ID>, ID extends Serializable> extends CrudService<T, ID> {

    <SR extends SearchRequest<Long>> Page<T> findAllWithoutCount(Pageable pageable, SR searchRequest);

    Page<T> findAll(Predicate predicate, Pageable pageable);

    <SR extends SearchRequest<Long>, K> Page<K> searchWithoutCountAndMap(Pageable pageable, SR searchRequest, Function<T, K> mapper, int limit, int offset);
}
