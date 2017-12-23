package com.balabala.web;

import lombok.Data;

@Data
public class ApiEntity {

    private int status;

    private String message;

    private Object data;

    public ApiEntity() {
        this.status = ApiStatus.STATUS_200.getCode();
        this.message = ApiStatus.STATUS_200.getMessage();
    }

    public ApiEntity(Object data) {
        this.status = ApiStatus.STATUS_200.getCode();
        this.message = ApiStatus.STATUS_200.getMessage();
        this.data = data;
    }

    public ApiEntity(ApiStatus apiStatus) {
        this.status = apiStatus.getCode();
        this.message = apiStatus.getMessage();
    }

    public ApiEntity(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
