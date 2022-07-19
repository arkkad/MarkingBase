package com.initflow.marking.base.service.nocount;

import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.domain.IDObj;
import com.initflow.marking.base.models.domain.QBaseEntity;
import com.initflow.marking.base.models.request.OffsetBasedPageRequest;
import com.initflow.marking.base.repository.QueryableReadRepository;
import com.initflow.marking.base.service.CrudServiceImpl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class CrudServiceImplWithoutCount<T extends IDObj<Long>> extends CrudServiceImpl<T, Long>
        implements ICrudServiceImplWithoutCount<T, Long> {

    protected abstract QueryableReadRepository<T, Long> getRepositoryNoCount();

    protected abstract <SR extends SearchRequest<Long>> BooleanBuilder getPredicate(SR searchRequest);

    protected abstract QBaseEntity getQBaseEntity();

    public <SR extends SearchRequest<Long>> Page<T> findAllWithoutCount(Pageable pageable, SR searchRequest, int limit, int offset) {
        OffsetBasedPageRequest offsetBasedPageRequest = new OffsetBasedPageRequest(offset, limit);
        var predicate = getPredicate(searchRequest);
        Sort.Order order = pageable.getSort().getOrderFor("id");
        if (order != null) {
            offsetBasedPageRequest.setDirection(order.getDirection());
            if (searchRequest.getLastIdPageable() != null) {
                switch (order.getDirection()) {
                    case ASC: {
                        predicate.and(getQBaseEntity().id.gt(searchRequest.getLastIdPageable()));
                        break;
                    }
                    case DESC: {
                        predicate.and(getQBaseEntity().id.lt(searchRequest.getLastIdPageable()));
                        break;
                    }
                }
            }
        } else {
            Long lastIdPageable = searchRequest.getLastIdPageable() != null ? searchRequest.getLastIdPageable() : 0L;
            predicate.and(getQBaseEntity().id.gt(lastIdPageable));
        }
        return getRepositoryNoCount().findAllWithoutCount(predicate, offsetBasedPageRequest);
    }


    public <SR extends SearchRequest<Long>, K> Page<K> searchWithoutCountAndMap(Pageable pageable, SR searchRequest, Function<T, K> mapper, int limit, int offset) {
        Page<T> page = this.findAllWithoutCount(pageable, searchRequest, limit, offset);
        return this.mappingWithoutCount(page, pageable, mapper, searchRequest);

    }

    private <K, SR extends SearchRequest<Long>> Page<K> mappingWithoutCount(Page<T> page, Pageable pageable, Function<T, K> mapper, SR searchRequest) {
        List<K> result = new ArrayList<>();
        page.getContent().forEach(it -> result.add(mapper.apply(it)));
        return new PageImpl<>(result, pageable, page.getTotalElements());
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        return getRepositoryNoCount().findAll(predicate, pageable);
    }

    @Override
    public <SR extends SearchRequest<Long>> Page<T> findAllWithoutCount(Pageable pageable, SR searchRequest) {
        return null;
    }

    @Override
    public <SR extends SearchRequest<Long>> long count(SR searchRequest) {
        return getRepositoryNoCount().count(getPredicate(searchRequest));
    }
}
