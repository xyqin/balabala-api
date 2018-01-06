package com.balabala.web.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String phoneNumber;

    private String code;

    private String password;

}
