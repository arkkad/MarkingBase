package com.initflow.marking.base.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListObjectWithTwoValuesSQLResponceParser {

    private ListObjectWithTwoValuesSQLResponceParser(){}

    public static Map<Long, Boolean> parseBoolValues(List<Object[]> objects){
        Map<Long, Boolean> eventUserCountMap = new HashMap<>();
        try{
            for(Object[] obj : objects){
                if(obj.length < 2 || !(obj[0] instanceof Long) || !(obj[1] instanceof Long)){
                    throw new RuntimeException("No right return data on get users count by event;");
                }
                Long eventId = (Long) obj[0];
                Long count = (Long) obj[1];
                boolean isSub = !(count == 0);
                eventUserCountMap.put(eventId, isSub);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return eventUserCountMap;
    }


    public static Map<Long, Long> parseLongValues(List<Object[]> objects){
        Map<Long, Long> eventUserCountMap = new HashMap<>();
        try{
            for(Object[] obj : objects){
                if(obj.length < 2 || !(obj[0] instanceof Long) || !(obj[1] instanceof Long)){
                    throw new RuntimeException("No right return data on get users count by event;");
                }
                Long event = (Long) obj[0];
                Long count = (Long) obj[1];
                eventUserCountMap.put(event, count);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return eventUserCountMap;
    }

    public static <K> Map<Long, K> parseValue(List<Object[]> objects, Class<K> zlass){
        Map<Long, K> eventUserCountMap = new HashMap<>();
        try{
            for(Object[] obj : objects){
                if(obj.length < 2 || !(obj[0] instanceof Long)
                    || (obj[1] == null || !(obj[1].getClass().isAssignableFrom(zlass)))){
                    throw new RuntimeException("No right return data on get users count by event;");
                }
                Long event = (Long) obj[0];
                K count = zlass.cast(obj[1]);
                eventUserCountMap.put(event, count);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return eventUserCountMap;
    }

    public static <K> Map<Long, List<K>> parseValueForList(List<Object[]> objects, Class<K> zlass){
        Map<Long, List<K>> eventUserCountMap = new HashMap<>();
        try{
            for(Object[] obj : objects){
                if(obj.length < 2 || !(obj[0] instanceof Long)
                    || (obj[1] == null || !(obj[1].getClass().isAssignableFrom(zlass)))){
                    throw new RuntimeException("No right return data on get users count by event;");
                }
                Long event = (Long) obj[0];
                K count = zlass.cast(obj[1]);
                MapListValueUtil.put(eventUserCountMap, event, count);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return eventUserCountMap;
    }


    public static <K,V> Map<K, List<V>> parseMapKKeyForListValues(List<Object[]> objects, Class<K> keyClass, Class<V> valueClass){
        Map<K, List<V>> result = new HashMap<>();
        try{
            for(Object[] obj : objects){
                if(obj.length < 2
                    || (obj[0] == null || !(obj[0].getClass().isAssignableFrom(keyClass)))
                    || (obj[1] == null || !(obj[1].getClass().isAssignableFrom(valueClass)))){
                    throw new RuntimeException("No right return data on get users count by event;");
                }
                K key = keyClass.cast(obj[0]);
                V value = valueClass.cast(obj[1]);
                MapListValueUtil.put(result, key, value);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <K,V> Map<K, V> parseMapKKeyForFirstValue(List<Object[]> objects, Class<K> keyClass, Class<V> valueClass){
        Map<K, V> result = new HashMap<>();
        return parseMapKKeyForFirstValue(objects, keyClass, valueClass, result);
    }



    public static <K,V> Map<K, V> parseMapKKeyForFirstValue(List<Object[]> objects, Class<K> keyClass, Class<V> valueClass, Map<K,V> result){
        try{
            for(Object[] obj : objects){
                if(obj.length < 2
                    || (obj[0] == null || !(obj[0].getClass().isAssignableFrom(keyClass)))
                    || (obj[1] == null || !(obj[1].getClass().isAssignableFrom(valueClass)))){
                    throw new RuntimeException("No right return data on get users count by event;");
                }
                K key = keyClass.cast(obj[0]);
                V value = valueClass.cast(obj[1]);
                result.putIfAbsent(key, value);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <K,V> Map<K, V> parseMapKKeyForFirstValueWithNullValues(List<Object[]> objects, Class<K> keyClass, Class<V> valueClass){
        Map<K, V> result = new HashMap<>();
        return parseMapKKeyForFirstValueWithNullValues(objects, keyClass, valueClass, result);
    }

    public static <K,V> Map<K, V> parseMapKKeyForFirstValueWithNullValues(List<Object[]> objects, Class<K> keyClass, Class<V> valueClass, Map<K,V> result){
        try{
            for(Object[] obj : objects){
                if(obj.length < 2
                        || (obj[0] != null && !(obj[0].getClass().isAssignableFrom(keyClass)))
                        || (obj[1] != null && !(obj[1].getClass().isAssignableFrom(valueClass)))){
                    throw new RuntimeException("No right return data on get users count by event;");
                }

                K key = obj[0] == null ? null : keyClass.cast(obj[0]);
                V value = obj[1] == null ? null : valueClass.cast(obj[1]);
                result.putIfAbsent(key, value);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }
}
