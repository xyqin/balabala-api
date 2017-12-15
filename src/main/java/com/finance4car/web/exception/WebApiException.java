package com.finance4car.web.exception;

/**
 * Created by xyqin on 2017/4/11.
 */
public class WebApiException extends RuntimeException {

    public WebApiException(String message) {
        super(message);
    }

    public WebApiException(String message, Throwable cause) {
        super(message, cause);
    }

}
