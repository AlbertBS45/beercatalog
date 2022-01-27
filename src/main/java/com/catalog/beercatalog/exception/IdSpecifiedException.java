package com.catalog.beercatalog.exception;


public class IdSpecifiedException extends RuntimeException {
    
    public IdSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdSpecifiedException(String message) {
        super(message);
    }

    public IdSpecifiedException(Throwable cause) {
        super(cause);
    }
}
