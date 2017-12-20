package com.balabala.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
public class ApplyClassRequest {

    @NotBlank
    private String teacherName;

    @NotNull
    private Long courseCategoryId;

    @NotNull
    private Long courseId;

    @NotBlank
    private String className;

}
