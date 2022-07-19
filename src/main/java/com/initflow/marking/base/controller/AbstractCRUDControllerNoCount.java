package com.initflow.marking.base.controller;

import com.initflow.marking.base.mapper.domain.CrudMapper;
import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.domain.IDObj;
import com.initflow.marking.base.permission.CheckDataPermission;
import com.initflow.marking.base.permission.PermissionPath;
import com.initflow.marking.base.service.nocount.ICrudServiceImplWithoutCount;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

public abstract class AbstractCRUDControllerNoCount<T extends IDObj<Long>, C_DTO, U_DTO, R_DTO, ID extends Serializable, SR extends SearchRequest<Long>>
        extends AbstractCRUDController<T, C_DTO, U_DTO, R_DTO, Long, SR> {

    private ICrudServiceImplWithoutCount<T, Long> crudService;

    public AbstractCRUDControllerNoCount(ICrudServiceImplWithoutCount<T, Long> crudService, CrudMapper<T, C_DTO, U_DTO, R_DTO> mapper) {
        super(crudService, mapper);
        this.crudService = crudService;
    }

    @PermissionPath({"#this.object.getReadPermissionPath()"})
    @CheckDataPermission("#this.object.getSearchPerm(#searchRequest, #request, #header)")
    @RequestMapping(
            value = {"/v2/search"},
            method = {RequestMethod.GET}
    )
    @ApiOperation(
            value = "Поиск сущности v2/Find documents v2",
            notes = "Поиск сущности v2/Find documents v2"
    )
    @ResponseBody
    public Page<R_DTO> readListWithoutCount(Pageable pageable, SR searchRequest, int limit,int offset, HttpServletRequest request, @RequestHeader Map<String, String> header) {
        return this.crudService.searchWithoutCountAndMap(pageable, searchRequest, this.getReadMapper(), limit, offset);
    }

    @PermissionPath("system:shoes:mrk:write")
    @ApiOperation(value = "Метод получения данных при множественном запросе КМ/\n" +
            "Method of obtaining data with multiple query KM")
    @RequestMapping(value = "/pageable", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<PageableResponse> count(Pageable pageable, SR searchRequest) {
        long totalElements = crudService.count(searchRequest);
        Integer totalPages = pageable.getPageSize() == 0 ? 1 : (int)Math.ceil((double)totalElements / (double)pageable.getPageSize());
        return ResponseEntity.ok(new PageableResponse(totalPages, totalElements));
    }

    public static class PageableResponse {
        private Integer totalPages;
        private Long totalElements;

        public PageableResponse() {
        }

        public PageableResponse(Integer totalPages, Long totalElements) {
            this.totalPages = totalPages;
            this.totalElements = totalElements;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }

        public Long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(Long totalElements) {
            this.totalElements = totalElements;
        }
    }

}
