package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaClass extends AbstractEntity<Long> {

    private Long courseId;

    private Long categoryId;

    private Long teacherId;

    private Long campusId;

    private Long englishTeacherId;

    private String className;

    private String monitor;

    private String monitorPhoneNumber;

    private ClassStatus status;

}
