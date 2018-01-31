package com.barablah.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class ResetPasswordRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String code;

    @NotBlank
    private String password;

}
