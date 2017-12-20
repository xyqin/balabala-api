package com.balabala.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by xyqin on 2017/6/2.
 */
@Data
public class SigninRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String password;

}
