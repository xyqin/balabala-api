package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaTeacherHomework extends AbstractEntity<Long> {

    private Long teacherId;

    private String homeworkName;

    private Date closingAt;

}