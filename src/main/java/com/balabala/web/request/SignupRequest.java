package com.balabala.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by xyqin on 2017/4/1.
 */
@Data
public class SignupRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String code;

    @NotBlank
    private String password;

    @NotNull
    @Min(value = 1)
    private Long campusId;

}
