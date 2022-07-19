package com.initflow.marking.base.util;


import java.util.*;

public class MapListValueUtil {

    private MapListValueUtil(){}

    public static <T,K> void put(Map<T, List<K>> map, T key, K value){
        List<K> valueByKey = map.get(key);
        if(valueByKey == null){
            List<K> list = new ArrayList<>();
            list.add(value);
            map.put(key, list);
        } else {
            valueByKey.add(value);
        }
    }

    public static <T,K> void putSet(Map<T, Set<K>> map, T key, K value){
        Set<K> valueByKey = map.get(key);
        if(valueByKey == null){
            Set<K> list = new HashSet<>();
            list.add(value);
            map.put(key, list);
        } else {
            valueByKey.add(value);
        }
    }
}
