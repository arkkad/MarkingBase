package com.initflow.marking.base.exception.model;

public class MetricsBaseRuntimeException extends RuntimeException {

    public MetricsBaseRuntimeException() {
        super();
    }

    public MetricsBaseRuntimeException(String message) {
        super(message);
    }

    public MetricsBaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetricsBaseRuntimeException(Throwable cause) {
        super(cause);
    }

}
