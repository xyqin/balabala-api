package com.balabala.web.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

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

    private List<Long> memberIds;

}
