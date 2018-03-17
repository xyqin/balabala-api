package com.barablah.web.request;

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
    private Long major;

    @NotBlank
    private String password;

    @NotBlank
    private Long from;

}
