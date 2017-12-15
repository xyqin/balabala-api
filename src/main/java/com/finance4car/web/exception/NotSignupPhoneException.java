package com.finance4car.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by xyqin on 2017/4/11.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotSignupPhoneException extends BadRequestException {

    public NotSignupPhoneException(String message) {
        super(message);
    }

    public NotSignupPhoneException(String message, Throwable cause) {
        super(message, cause);
    }

}
