package com.initflow.marking.base.exception;

import com.initflow.marking.base.exception.model.*;
import com.initflow.marking.base.mapper.error.ErrorVM;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {


    @ExceptionHandler(HttpMessageNotReadableBaseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorVM processMethodNotSupportedException(HttpMessageNotReadableBaseException exception) {
        return new ErrorVM(HttpStatus.BAD_REQUEST.toString() + "; " + exception.getMessage(), null, ExceptionUtils.getMessage(exception));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorVM processMethodNotSupportedException(HttpMessageNotReadableException exception) {
        return new ErrorVM(HttpStatus.BAD_REQUEST.toString() + "; " + exception.getMessage(), null, ExceptionUtils.getMessage(exception));
    }

    @ExceptionHandler(MetricsBaseRuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorVM processMethodNotSupportedException(MetricsBaseRuntimeException exception) {
        return new ErrorVM(HttpStatus.BAD_REQUEST.toString() + "; " + exception.getMessage(), null, ExceptionUtils.getMessage(exception));
    }

    @ExceptionHandler(ForbiddenMetricsBaseRuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorVM processMethodNotSupportedException(ForbiddenMetricsBaseRuntimeException exception) {
        return new ErrorVM(HttpStatus.FORBIDDEN.toString() + "; " + exception.getMessage(), null, ExceptionUtils.getMessage(exception));
    }

    @ExceptionHandler(UnprocessableMetricsBaseRuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorVM processMethodNotSupportedException(UnprocessableMetricsBaseRuntimeException exception) {
        return new ErrorVM(HttpStatus.UNPROCESSABLE_ENTITY.toString() + "; " + exception.getMessage(), null, ExceptionUtils.getMessage(exception));
    }

    @ExceptionHandler(NotFoundMetricsBaseException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorVM processMethodNotSupportedException(NotFoundMetricsBaseException exception) {
        return new ErrorVM(HttpStatus.NOT_FOUND.toString() + "; " + exception.getMessage(), null, ExceptionUtils.getMessage(exception));
    }

    @ExceptionHandler(NotAllowedMetricsBaseRuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorVM processMethodNotSupportedException(NotAllowedMetricsBaseRuntimeException exception) {
        return new ErrorVM(HttpStatus.METHOD_NOT_ALLOWED.toString() + "; " + exception.getMessage(), null, ExceptionUtils.getMessage(exception));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorVM> processRuntimeException(Exception ex) {
        BodyBuilder builder;
        ErrorVM errorVM;
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            errorVM = new ErrorVM("error." + responseStatus.value().value(), responseStatus.reason(), ex.getMessage());
        } else {
            builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            errorVM = new ErrorVM("error.internalServerError", "Internal server error",  ExceptionUtils.getMessage(ex));
        }
        return builder.body(errorVM);
    }
}
