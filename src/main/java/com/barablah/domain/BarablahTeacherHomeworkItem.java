package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahTeacherHomeworkItem extends AbstractEntity<Long> {

    private Long teacherId;

    private Long homeworkId;

    private Long textbookId;

}