package com.barablah.web;

public enum ApiStatus {

    STATUS_200(0, "OK"),
    STATUS_400(400, "Bad Request"),
    STATUS_401(401, "Unauthorized"),
    STATUS_403(403, "Forbidden"),
    STATUS_404(404, "Not Found"),
    STATUS_500(500, "Internal Server Error");

    ApiStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
