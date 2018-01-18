package com.barablah.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ResetPasswordRequest {

    @NotNull
    private String phoneNumber;

    @NotNull
    private String code;

    @NotNull
    private String password;

}
