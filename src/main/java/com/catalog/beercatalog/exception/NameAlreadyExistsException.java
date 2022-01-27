package com.catalog.beercatalog.exception;


public class NameAlreadyExistsException extends RuntimeException {
    
    public NameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NameAlreadyExistsException(String message) {
        super(message);
    }

    public NameAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
