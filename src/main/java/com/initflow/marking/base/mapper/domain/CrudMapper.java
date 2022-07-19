package com.initflow.marking.base.mapper.domain;

public interface CrudMapper<T, C_DTO, U_DTO, R_DTO> {
    T createMapping(C_DTO obj);
    void updateMapping(U_DTO from, T to);
    R_DTO readMapping(T obj);
}
