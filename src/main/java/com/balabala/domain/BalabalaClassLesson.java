package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaClassLesson extends AbstractEntity<Long> {

    private Long classId;

    private Long courseId;

    private Long teacherId;

    private Long englishTeacherId;

    private String lessonName;

    private Date startAt;

    private Date endAt;

    private String room;

}
