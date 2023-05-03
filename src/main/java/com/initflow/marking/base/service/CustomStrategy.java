package com.initflow.marking.base.service;

import com.opencsv.bean.ColumnPositionMappingStrategy;

public class CustomStrategy<T> extends ColumnPositionMappingStrategy<T> {
    @Override
    public String[] generateHeader() {
        return this.getColumnMapping();
    }
}