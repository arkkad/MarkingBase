package com.initflow.marking.base.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Set;

public class SearchRequest<T> {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private Set<T> ids;
    private T lastId;
    private T lastIdPageable;

    public Set<T> getIds() {
        return ids;
    }

    public void setIds(Set<T> ids) {
        this.ids = ids;
    }

    public T getLastId() {
        return lastId;
    }

    public void setLastId(T lastId) {
        this.lastId = lastId;
    }

    public T getLastIdPageable() {
        return lastIdPageable;
    }

    public void setLastIdPageable(T lastIdPageable) {
        this.lastIdPageable = lastIdPageable;
    }
}
