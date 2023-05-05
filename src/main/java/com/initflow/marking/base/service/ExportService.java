package com.initflow.marking.base.service;

import com.initflow.marking.base.mapper.domain.CrudMapper;
import com.initflow.marking.base.models.SearchRequest;
import com.initflow.marking.base.models.domain.IDObj;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ExportService<T extends IDObj<ID>, C_DTO, U_DTO, R_DTO, ID extends Serializable, SR extends SearchRequest> {
    private CrudService<T, ID> crudService;
    private CrudMapper<T, C_DTO, U_DTO, R_DTO> mapper;

    private MailService exportMailService;

    protected ExportService(CrudService<T, ID> crudService, CrudMapper<T, C_DTO, U_DTO, R_DTO> mapper, MailService exportMailService) {
        this.crudService = crudService;
        this.mapper = mapper;
        this.exportMailService = exportMailService;
    }

    public void export(
            SR searchRequest,
            List<String> mails,
            String company,
            String wh,
            String pg
    ) throws IOException {
        int exported = 0;
        int currPage = 0;
        int exportPlan = 50_000;
        List<R_DTO> records = new ArrayList<>();
        Page<T> page = this.crudService.findAll(PageRequest.of(currPage, 200), searchRequest);
        T t = page.stream().findFirst().orElseThrow();
        R_DTO r_dto = mapper.readMapping(t);
        while (currPage <= page.getTotalPages()) {
            page = this.crudService.findAll(PageRequest.of(currPage, 2000), searchRequest);
            List<T> content = page.getContent();

            List<R_DTO> endpoints = content.stream()
                    .map(mapper::readMapping)
                    .collect(Collectors.toList());

            records.addAll(endpoints);
            exported += page.getContent().size();
            currPage += 1;
            if (exported >= exportPlan) {
                //
                List<String> columns = Arrays.stream(FieldUtils.getAllFields(t.getClass()))
                        .map(Field::getName).collect(Collectors.toList());

                CustomStrategy<R_DTO> mappingStrategy =
                        new CustomStrategy<>();
                mappingStrategy.setColumnMapping(columns.toArray(String[]::new));
                mappingStrategy.setType((Class<? extends R_DTO>) r_dto.getClass());

                ByteArrayOutputStream out = new ByteArrayOutputStream();

                try (Writer writer = new OutputStreamWriter(out);) {
                    StatefulBeanToCsvBuilder<R_DTO> builder =
                            new StatefulBeanToCsvBuilder<>(writer);
                    StatefulBeanToCsv<R_DTO> beanWriter =
                            builder.withMappingStrategy(mappingStrategy)
                                    .withSeparator(';')
                                    .build();
                    beanWriter.write(records);

                    exportMailService.send(out, mails, company, wh, pg);

                } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
                    exportMailService.sendError(mails, e, company, wh, pg);
                }
                records = new ArrayList<>();
                exported = 0;
                //
            }
        }

        List<String> columns = Arrays.stream(FieldUtils.getAllFields(t.getClass()))
                .map(Field::getName).collect(Collectors.toList());

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

            exportMailService.send(out, mails, company, wh, pg);

        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            exportMailService.sendError(mails, e, company, wh, pg);
        }
    }
}
