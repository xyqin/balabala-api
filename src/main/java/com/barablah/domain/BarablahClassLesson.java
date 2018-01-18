package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahClassLesson extends AbstractEntity<Long> {

    private Long classId;

    private Long courseId;

    private Long teacherId;

    private Long englishTeacherId;

    private Long categoryId;

    private String lessonName;

    private Date startAt;

    private Date endAt;

    private String thumbnail;

    private String video;

    private String room;

    private Boolean prepared;

    private LessonType type;

}
