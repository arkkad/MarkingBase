package com.initflow.marking.base.mapper.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * View Model for transferring error message with a list of field errors.
 */
public class ErrorVM implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String message;
    private final String description;
    private final String exMessage;

    private List<FieldErrorVM> fieldErrors;

//    public ErrorVM(String message, String exMessage) {
//        this(message, null, exMessage);
//    }

    public ErrorVM(String message, String description, String exMessage) {
        this.message = message;
        this.description = description;
        this.exMessage = exMessage;
    }

    public ErrorVM(String message, String description, String exMessage, List<FieldErrorVM> fieldErrors) {
        this.message = message;
        this.description = description;
        this.exMessage = exMessage;
        this.fieldErrors = fieldErrors;
    }

    public void add(String objectName, String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorVM(objectName, field, message));
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public List<FieldErrorVM> getFieldErrors() {
        return fieldErrors;
    }

    public String getExMessage(){
        return exMessage;
    }
}
