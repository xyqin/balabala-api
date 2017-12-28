package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaTeacherHomeworkItem extends AbstractEntity<Long> {

    private Long teacherId;

    private Long homeworkId;

    private Long textbookId;

}