package com.barablah.config;

import com.barablah.web.exception.WebApiException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;

import java.util.Map;

/**
 * Created by xyqin on 2017/4/3.
 */
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
        Throwable exception = getError(requestAttributes);

        if (exception instanceof WebApiException) {
            errorAttributes.put("message", exception.getMessage());
        } else if (exception instanceof MethodArgumentNotValidException) {
            FieldError error = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldError();
            errorAttributes.put("message", error.getField() + StringUtils.SPACE + error.getDefaultMessage());
        }

        return errorAttributes;
    }

}
