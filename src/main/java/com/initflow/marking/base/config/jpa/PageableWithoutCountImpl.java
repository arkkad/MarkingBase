package com.initflow.marking.base.config.jpa;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableWithoutCountImpl<T> extends AbstractPageRequest implements PageableWithoutCount {

    private T lastId;
    private Sort sort;

    public PageableWithoutCountImpl(T lastId, int size) {
        super(0, size);
        this.lastId = lastId;
        sort = Sort.by(Sort.Direction.ASC, "id");
    }

    public PageableWithoutCountImpl(T lastId, int size, Sort.Direction direction) {
        super(0, size);
        this.lastId = lastId;
        sort = Sort.by(direction, "id");
    }


    @Override
    public T getLastId() {
        return lastId;
    }

    public void setDirection(Sort.Direction direction){
        sort = Sort.by(direction, "id");
        System.out.println();
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previous() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }
}
