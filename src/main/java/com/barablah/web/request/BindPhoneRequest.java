package com.barablah.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class BindPhoneRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String code;

    @NotBlank
    private String password;

    @NotNull
    private Long campusId;

}
