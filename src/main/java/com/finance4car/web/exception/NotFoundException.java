package com.finance4car.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by xyqin on 2017/4/7.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends WebApiException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
