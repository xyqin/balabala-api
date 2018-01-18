package com.balabala.web.request;

import com.google.common.collect.Lists;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ApplyClassRequest {

    @NotBlank
    private String teacherName;

    @NotNull
    private Long courseId;

    @NotBlank
    private String className;

    private List<Long> memberIds = Lists.newArrayList();

}
