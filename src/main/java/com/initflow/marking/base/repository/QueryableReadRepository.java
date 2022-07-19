package com.initflow.marking.base.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;


@NoRepositoryBean
public interface QueryableReadRepository<T, ID> extends Repository<T, ID> {

    Page<T> findAllWithoutCount(BooleanBuilder predicate, Pageable pageable);
    Page<T> findAll(Predicate predicate, Pageable pageable);
    long count(Predicate predicate);

}
