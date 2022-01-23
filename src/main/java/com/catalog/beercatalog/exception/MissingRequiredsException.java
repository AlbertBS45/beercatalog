package com.catalog.beercatalog.exception;


public class MissingRequiredsException extends RuntimeException {
    
    public MissingRequiredsException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingRequiredsException(String message) {
        super(message);
    }

    public MissingRequiredsException(Throwable cause) {
        super(cause);
    }
}
