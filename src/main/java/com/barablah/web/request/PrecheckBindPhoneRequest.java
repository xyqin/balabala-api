package com.barablah.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class PrecheckBindPhoneRequest {

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String code;

}
