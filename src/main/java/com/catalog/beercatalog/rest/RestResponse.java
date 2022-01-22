package com.catalog.beercatalog.rest;

import lombok.Data;

@Data
public class RestResponse {
    
    private int status;

    private String message;

    private String currentTimeStamp;

    public RestResponse() {}

    public RestResponse(int status, String message, String currentTimeStamp) {
        this.status = status;
        this.message = message;
        this.currentTimeStamp = currentTimeStamp;
    }
}
