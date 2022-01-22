package com.catalog.beercatalog.exception;

import java.util.Date;

import com.catalog.beercatalog.rest.RestResponse;
import com.catalog.beercatalog.utils.DateFormatUtil;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    public ResponseEntity<RestResponse> handleException(Exception exception) {

        RestResponse response = new RestResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exception.getMessage());
        response.setCurrentTimeStamp(DateFormatUtil.getFormattedDate(new Date()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
