package com.initflow.marking.base.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.initflow.marking.base.exception.model.HttpMessageNotReadableBaseException;
import com.initflow.marking.base.mapper.domain.CrudMapper;
import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.domain.IDObj;
import com.initflow.marking.base.permission.CheckDataPermission;
import com.initflow.marking.base.permission.PermissionPath;
import com.initflow.marking.base.service.CrudService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class AbstractCRUDController<T extends IDObj<ID>, C_DTO, U_DTO, R_DTO, ID extends Serializable, SR extends SearchRequest> {

    private CrudService<T, ID> crudService;
    private CrudMapper<T, C_DTO, U_DTO, R_DTO> mapper;

    @Autowired
    ObjectMapper objectMapper;

//    private CRUDPermission permission;

    public AbstractCRUDController(CrudService<T, ID> crudService, CrudMapper<T, C_DTO, U_DTO, R_DTO> mapper){
        this.crudService = crudService;
        this.mapper = mapper;
//        this.permission = new DefaultCRUDPermissionImpl();
    }

    @PermissionPath("#this.object.getReadPermissionPath()")
    @CheckDataPermission("#this.object.getReadListPerm(#ids, #request, #header)")
//    @PreAuthorize("hasAnyAuthority(#root.this.getReadRoles()) and #root.this.getReadPerm(#id, #request, #header)")
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Получение списка сущностей/Get documents", notes = "Получение списка сущностей/Get documents")
    public @ResponseBody
    Page<R_DTO> readList(@RequestParam(value = "ids", required = false) List<ID> ids, Pageable pageable, HttpServletRequest request, @RequestHeader Map<String, String> header) {
        return crudService.readList(ids, pageable, getReadMapper());
//        R_DTO dto = crudService.read(id, getReadMapper());
//        return ResponseEntity.ok(dto);
    }

    @PermissionPath("#this.object.getReadPermissionPath()")
    @CheckDataPermission("#this.object.getSearchPerm(#searchRequest, #request, #header)")
//    @PreAuthorize("hasAnyAuthority(#root.this.getReadRoles()) and #root.this.getSearchPerm(#searchRequest, #request, #header)")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "Поиск сущности/Find documents", notes = "Поиск сущности/Find documents")
    public @ResponseBody
    Page<R_DTO> readList(Pageable pageable, SR searchRequest,
                         HttpServletRequest request, @RequestHeader Map<String, String> header) {
        return crudService.searchAndMap(pageable, searchRequest, getReadMapper());
//        R_DTO dto = crudService.read(id, getReadMapper());
//        return ResponseEntity.ok(dto);
    }

    @PermissionPath("#this.object.getReadPermissionPath()")
    @CheckDataPermission("#this.object.getReadPerm(#id, #request, #header)")
//    @PreAuthorize("hasAnyAuthority(#root.this.getReadRoles()) and #root.this.getReadPerm(#id, #request, #header)")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Получить сущность по id/Read document", notes = "Получить сущность по id/Read document")
    public @ResponseBody
    ResponseEntity<R_DTO> read(@PathVariable ID id,
                               HttpServletRequest request, @RequestHeader Map<String, String> header) {
        R_DTO dto = crudService.read(id, getReadMapper());
        return ResponseEntity.ok(dto);
    }

    @PermissionPath("#this.object.getUpdatePermissionPath()")
    @CheckDataPermission("#this.object.getUpdatePerm(#id, #dto, #request, #header)")
//    @PreAuthorize("hasAnyAuthority(#root.this.updateRoles) and #root.this.getUpdatePerm(#id, #dto, #request, #header)")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "Обновление сущности/Update document", notes = "Обновление сущности/Update document")
    public @ResponseBody
    ResponseEntity<R_DTO> update(@PathVariable ID id, @RequestBody U_DTO dto,
                                 HttpServletRequest request, @RequestHeader Map<String, String> header) {
        T obj = crudService.update(id, dto, getUpdateMapper());
        R_DTO respDTO = getReadMapper().apply(obj);
        postCreateFunc(obj);
        return ResponseEntity.ok(respDTO);
    }

    @PermissionPath({"#this.object.getUpdatePermissionPath()"})
    @CheckDataPermission("#this.object.getPatchPerm(#id, #webRequest)")
    @RequestMapping(value = {"/{id}"}, method = {RequestMethod.PATCH})
    @ApiOperation(value = "Обновление сущности/Update document", notes = "Обновление сущности/Update document")
    @ResponseBody
    public ResponseEntity<R_DTO> patchUpdate(@PathVariable ID id, NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        var req =  new ServletServerHttpRequest(servletRequest);

        T obj = crudService.findOne(id).orElse(null);
        if(obj == null)
            return ResponseEntity.ok(null);
        T updatedObj  = readJavaType(obj, req);
        updatedObj.setId(obj.getId());
        updatedObj = crudService.save(updatedObj);
        R_DTO respDTO = getReadMapper().apply(updatedObj);
        postCreateFunc(obj);
        return ResponseEntity.ok(respDTO);
    }

    private T readJavaType(Object object, HttpInputMessage inputMessage) {
        try {
            return this.objectMapper.readerForUpdating(object).readValue(inputMessage.getBody());
        }
        catch (IOException ex) {
            throw new HttpMessageNotReadableBaseException("Could not read document: " + ex.getMessage(), ex);
        }
    }

    @PermissionPath("#this.object.getCreatePermissionPath()")
    @CheckDataPermission("#this.object.getCreatePerm(#dto, #request, #header)")
//    @PreAuthorize("hasAnyAuthority(#root.this.createRoles) and #root.this.getCreatePerm(#dto, #request, #header)")
    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "Создание сущости/Create document", notes = "Создание сущости/Create document")
    public @ResponseBody
    ResponseEntity<R_DTO> create(@RequestBody C_DTO dto,
                                 HttpServletRequest request, @RequestHeader Map<String, String> header) {
        T obj = crudService.create(dto, getCreateMapper());
        R_DTO respDTO = getReadMapper().apply(obj);
        postCreateFunc(obj);
        return ResponseEntity.ok(respDTO);
    }

    protected Function<T, R_DTO> getReadMapper() {
        return (T obj) -> mapper.readMapping(obj);
    }

    protected BiConsumer<U_DTO, T> getUpdateMapper() {
        return (U_DTO from, T to) -> mapper.updateMapping(from, to);
    }

    protected Function<C_DTO, T> getCreateMapper() {
        return (C_DTO dto) -> mapper.createMapping(dto);
    }

//    public abstract String[] getReadRoles();
//
//    public abstract String[] getUpdateRoles();
//
//    public abstract String[] getCreateRoles();

    public abstract String getReadPermissionPath();

    public abstract String getUpdatePermissionPath();

    public abstract String getCreatePermissionPath();

    public boolean getReadPerm(ID id, HttpServletRequest request, Map<String, String> header){
        return true;
    }

    public boolean getSearchPerm(SR searchRequest, HttpServletRequest request, Map<String, String> header){
        return true;
    }

    public boolean getUpdatePerm(ID id, U_DTO dto, HttpServletRequest request, Map<String, String> header){
        return true;
    }

    public boolean getPatchPerm(ID id, NativeWebRequest webRequest){
        return true;
    }

    public boolean getCreatePerm(C_DTO dto, HttpServletRequest request, Map<String, String> header){
        return true;
    }

    public boolean getReadListPerm(List<ID> ids, HttpServletRequest request, Map<String, String> header){
        return true;
    }

    public void postCreateFunc(T obj){}
//    public void postReadFunc(T obj){}
    public void postUpdateFunc(T obj){}

}