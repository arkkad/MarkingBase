package com.initflow.marking.base.util.map;

import com.initflow.marking.base.mapper.domain.EntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Tom on 21.08.2017.
 */
public class CastEndpointPageUtil {

    private CastEndpointPageUtil(){}

    public static <T, V> Page<V> map(EntityMapper<V, T> entityMapper, Page<T> page, Pageable pageable){
        if(page.getContent() == null) return null;
        List<V> entityDtos = entityMapper.toDto(page.getContent());
        return new PageImpl<V>(entityDtos, pageable, page.getTotalElements());
    }
}
