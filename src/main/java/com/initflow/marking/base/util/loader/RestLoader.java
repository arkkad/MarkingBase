package com.initflow.marking.base.util.loader;

import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Consumer;

public class RestLoader {

    private Consumer<RestTemplate> restTemplateInfo;

    public RestLoader() {
    }

    public RestLoader(Consumer<RestTemplate> restTemplateInfo) {
        this.restTemplateInfo = restTemplateInfo;
    }

    public <T> T get(String url, Map<String, String> headers, Class<T> zlass) {
        return getEntity(url, headers, zlass).getBody();
    }

    public <T> ResponseEntity<T> getEntity(String url, Map<String, String> headers, Class<T> zlass) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(getMessageConverters());

        if (restTemplateInfo != null) {
            restTemplateInfo.accept(restTemplate);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        HttpEntity entity = new HttpEntity(httpHeaders);

        return restTemplate.exchange(url, HttpMethod.GET, entity, zlass);
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);

        return messageConverters;
    }

    public <T, K> T post(String url, K params, Map<String, String> headers, Class<T> zlass) {
        return postEntity(url, params, headers, zlass).getBody();
    }

    public <T, K> ResponseEntity<T> postEntity(String url, K params, Map<String, String> headers, Class<T> zlass) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(getMessageConverters());

        if (restTemplateInfo != null) {
            restTemplateInfo.accept(restTemplate);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        HttpEntity<K> entity = new HttpEntity<>(params, httpHeaders);

        return restTemplate.postForEntity(url, entity, zlass);
    }

    public <K> void put(String url, K params, Map<String, String> headers) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(getMessageConverters());

        if (restTemplateInfo != null) {
            restTemplateInfo.accept(restTemplate);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        HttpEntity<K> entity = new HttpEntity<>(params, httpHeaders);

        restTemplate.put(url, entity);
    }


    public <T, K> T getObjectByRequest(String httpMethod, String url, K params, Map<String, String> headers,
                                       Class<T> zlass) {
        headers = headers != null ? headers : new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        switch (httpMethod) {
            case "GET":
                return get(url, headers, zlass);
            case "POST":
                return post(url, params, headers, zlass);
            case "PUT":
                put(url, params, headers);
                return null;
        }
        return null;
    }


    public <T, K> ResponseEntity<T> getResponseEntityByRequest(String httpMethod, String url, K params, Map<String, String> headers,
                                                               Class<T> zlass) {
        headers = headers != null ? headers : new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        switch (httpMethod) {
            case "GET":
                return getEntity(url, headers, zlass);
            case "POST":
                return postEntity(url, params, headers, zlass);
            case "PUT":
                put(url, params, headers);
                return null;
        }
        return null;
    }
}
