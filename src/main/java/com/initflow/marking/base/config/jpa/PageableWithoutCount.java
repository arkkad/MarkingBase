package com.initflow.marking.base.config.jpa;

import org.springframework.data.domain.Pageable;

public interface PageableWithoutCount<T> extends Pageable {

    T getLastId();
}
