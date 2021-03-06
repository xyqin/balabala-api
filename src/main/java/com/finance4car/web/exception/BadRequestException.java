package com.finance4car.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by xyqin on 2017/4/3.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends WebApiException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
