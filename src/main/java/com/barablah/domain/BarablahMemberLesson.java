package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahMemberLesson extends AbstractEntity<Long> {

    private Long memberId;

    private Long classId;

    private Long lessonId;

    private Date startAt;

    private Date endAt;

    private LessonType type;

    private Boolean probational;

}