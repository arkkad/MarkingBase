package com.initflow.marking.base.config.jpa;

import com.initflow.marking.base.repository.QueryableReadRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class QueryableReadRepositoryImpl<T> extends QuerydslJpaPredicateExecutor<T> implements QueryableReadRepository<T, Long> {

    private static final EntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;

    private final EntityPath<T> path;
    private final PathBuilder<T> builder;
    private final Querydsl querydsl;

    public QueryableReadRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
                                       EntityManager entityManager) {
        super(entityInformation, entityManager, resolver, null);
        this.path = resolver.createPath(entityInformation.getJavaType());
        this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    @Override
    public Page<T> findAllWithoutCount(BooleanBuilder predicate, Pageable pageable) {
        Assert.notNull(pageable, "Pageable must not be null!");
//        predicate.and(QBaseEntity.baseEntity.id.gt(pageable.getLastId()));
        JPQLQuery<T> query = this.querydsl.applyPagination(pageable, this.createQuery(predicate).select(this.path));
        List var10000 = query.fetch();
        return PageableExecutionUtils.getPage(var10000, pageable, () -> 0);
    }

}
