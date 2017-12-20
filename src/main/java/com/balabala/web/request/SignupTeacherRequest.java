package com.balabala.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class SignupTeacherRequest {

    @NotNull
    private Long campusId;

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String major;

    @NotBlank
    private String from;

}
