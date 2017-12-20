package com.balabala.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class SigninTeacherRequest {

    @NotBlank
    private String loginName;

    @NotBlank
    private String password;

}
