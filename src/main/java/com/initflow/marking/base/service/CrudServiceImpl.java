package com.initflow.marking.base.service;

import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.domain.IDObj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class CrudServiceImpl<T extends IDObj<ID>, ID extends Serializable> extends ReadAndSaveServiceImpl<T,ID>
        implements CrudService<T, ID> {

    public <K> T create(K dto, Function<K,T> mapper){
        T obj = mapper.apply(dto);
        obj.setId(null);
        return save(obj);
    }

//    public abstract <K> T create(K dto, Function<K, T> mapper, String username);

    public <K> T update(ID id, K dto, BiConsumer<K, T> mapper){
        Optional<T> obj = findOne(id);
        if(obj.isPresent()){
            T to = obj.get();
            mapper.accept(dto, to);
            to.setId(id);
            return save(to);
        }
        return null;
    }

    public <K> K read(ID id, Function<T,K> mapper){
        Optional<T> obj = findOne(id);
        return obj.map(mapper).orElse(null);
    }

    public <K> Page<K> readList(List<ID> ids, Pageable pageable, Function<T,K> mapper){
        Page<T> page = ids != null && ids.size() > 0 ? findByids(ids, pageable) : findAll(pageable);
        return mapping(page, pageable, mapper);
//        List<K> result = new ArrayList<>();
//        if(page.getContent() != null) page.getContent().forEach(it -> result.add(mapper.apply(it)));
//        return new PageImpl<>(result, pageable, page.getTotalElements());
    }

    public abstract Page<T> findByids(List<ID> ids, Pageable pageable);

    public <SR extends SearchRequest<ID>, K> Page<K> searchAndMap(Pageable pageable, SR searchRequest, Function<T,K> mapper){
        Page<T> page = findAll(pageable, searchRequest);
        return mapping(page, pageable, mapper);
    }

    private <K> Page<K> mapping(Page<T> page, Pageable pageable, Function<T,K> mapper){
        List<K> result = new ArrayList<>();
        page.getContent().forEach(it -> result.add(mapper.apply(it)));
        return new PageImpl<>(result, pageable, page.getTotalElements());
    }

    public abstract <SR extends SearchRequest<ID>> Page<T> findAll(Pageable pageable, SR searchRequest);

    public <SR extends SearchRequest<ID>> T findFirst(SR sr) {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Page<T> result = findAll(pageRequest, sr);

        return result.getContent().size() != 0 ? result.getContent().get(0) : null;
    }

    public abstract <SR extends SearchRequest<ID>> long count(SR searchRequest);
}
