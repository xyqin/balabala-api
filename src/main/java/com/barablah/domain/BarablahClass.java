package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahClass extends AbstractEntity<Long> {

    private Long courseId;

    private Long teacherId;

    private Long campusId;

    private Long englishTeacherId;

    private String className;

    private String monitor;

    private String monitorPhoneNumber;

    private String status;

}
