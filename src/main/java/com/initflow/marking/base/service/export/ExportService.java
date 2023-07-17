package com.initflow.marking.base.service.export;

import com.initflow.marking.base.exception.model.MetricsBaseRuntimeException;
import com.initflow.marking.base.mapper.domain.CrudMapper;
import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.SortingProperties;
import com.initflow.marking.base.models.domain.IDObj;
import com.initflow.marking.base.service.CrudService;
import com.initflow.marking.base.service.CustomStrategy;
import com.initflow.marking.base.service.storage.StorageService;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ExportService<T extends IDObj<ID>, C_DTO, U_DTO, R_DTO, ID extends Serializable, SR extends SearchRequest> {
    private final CrudService<T, ID> crudService;
    private final CrudMapper<T, C_DTO, U_DTO, R_DTO> mapper;
    private final StorageService storageService;

    protected ExportService(
            CrudService<T, ID> crudService,
            CrudMapper<T, C_DTO, U_DTO, R_DTO> mapper,
            StorageService storageService
    ) {
        this.crudService = crudService;
        this.mapper = mapper;
        this.storageService = storageService;
    }

    public String export(SR searchRequest, SortingProperties sortingProperties, String username, Integer userCount) {
        List<String> nonSuitableColumns = List.of("Id", "id", "certificates", "errors");
        int currPage = 0;
        int exported = 0;
        List<R_DTO> records = new ArrayList<>();
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortingProperties.getOrder()), sortingProperties.getField());
        Page<T> page = this.crudService.findAll(PageRequest.of(currPage, 1, Sort.by(order)), searchRequest);
        T t = page.stream().findFirst().orElseThrow();
        R_DTO readDto = mapper.readMapping(t);

        List<String> columns = new ArrayList<>();
        columns.add("id");
        Arrays.stream(FieldUtils.getAllFields(readDto.getClass()))
                .map(Field::getName)
                .filter(it -> !nonSuitableColumns.contains(it))
                .forEach(columns::add);

        while (currPage <= page.getTotalPages() && exported <= userCount) {
            page = this.crudService.findAll(PageRequest.of(currPage, 200, Sort.by(order)), searchRequest);
            List<T> content = page.getContent();

            List<R_DTO> endpoints = content.stream()
                    .map(mapper::readMapping)
                    .collect(Collectors.toList());

            records.addAll(endpoints);
            currPage += 1;
            exported += content.size();
        }
        return saveExpotReportToS3Storage(columns,
                readDto,
                records,
                t.getClass().getSimpleName(),
                username
        );
    }

    private String saveExpotReportToS3Storage(
            List<String> columns,
            R_DTO r_dto,
            List<R_DTO> records,
            String journalName,
            String username
    ) {

        CustomStrategy<R_DTO> mappingStrategy =
                new CustomStrategy<>();
        mappingStrategy.setColumnMapping(columns.toArray(String[]::new));
        mappingStrategy.setType((Class<? extends R_DTO>) r_dto.getClass());

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (OutputStreamWriter writer = new OutputStreamWriter(out);) {
            StatefulBeanToCsvBuilder<R_DTO> builder =
                    new StatefulBeanToCsvBuilder<>(writer);
            StatefulBeanToCsv<R_DTO> beanWriter =
                    builder.withMappingStrategy(mappingStrategy)
                            .withSeparator(';')
                            .build();


            beanWriter.write(records);
            writer.flush();

            return storageService.save(out, journalName, username);

        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | IOException e) {
            throw new MetricsBaseRuntimeException(e);
        }
    }
}
