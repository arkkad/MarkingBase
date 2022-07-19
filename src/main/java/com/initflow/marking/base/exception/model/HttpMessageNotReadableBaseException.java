package com.initflow.marking.base.exception.model;

public class HttpMessageNotReadableBaseException extends MetricsBaseRuntimeException {

    public HttpMessageNotReadableBaseException() {
        super();
    }

    public HttpMessageNotReadableBaseException(String message) {
        super(message);
    }

    public HttpMessageNotReadableBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpMessageNotReadableBaseException(Throwable cause) {
        super(cause);
    }

}
