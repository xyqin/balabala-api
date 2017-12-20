package com.balabala.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by xyqin on 2017/6/9.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends WebApiException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

}
