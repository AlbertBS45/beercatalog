package com.catalog.beercatalog.exception;

import java.util.Date;

import com.catalog.beercatalog.rest.RestResponse;
import com.catalog.beercatalog.utils.DateFormatUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {
    
    @ExceptionHandler
    public ResponseEntity<RestResponse> handleException(NotFoundException exception) {

        RestResponse response = new RestResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setCurrentTimeStamp(DateFormatUtil.getFormattedDate(new Date()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<RestResponse> handleException(IdSpecifiedException exception) {

        RestResponse response = new RestResponse();
        response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.setMessage(exception.getMessage());
        response.setCurrentTimeStamp(DateFormatUtil.getFormattedDate(new Date()));

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<RestResponse> handleException(MissingRequiredsException exception) {

        RestResponse response = new RestResponse();
        response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.setMessage(exception.getMessage());
        response.setCurrentTimeStamp(DateFormatUtil.getFormattedDate(new Date()));

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler
    public ResponseEntity<RestResponse> handleException(NameAlreadyExistsException exception) {

        RestResponse response = new RestResponse();
        response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        response.setMessage(exception.getMessage());
        response.setCurrentTimeStamp(DateFormatUtil.getFormattedDate(new Date()));

        return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<RestResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {

        RestResponse response = new RestResponse();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setMessage("The authenticated user does not have the necessary permissions to access the resource.");
        response.setCurrentTimeStamp(DateFormatUtil.getFormattedDate(new Date()));

        return new ResponseEntity<RestResponse>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ResponseEntity<RestResponse> handleException(Exception exception) {

        RestResponse response = new RestResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exception.getMessage());
        response.setCurrentTimeStamp(DateFormatUtil.getFormattedDate(new Date()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
