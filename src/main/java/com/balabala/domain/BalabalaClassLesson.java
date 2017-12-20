package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaClassLesson extends AbstractEntity<Long> {

    private Long classId;

    private String lessonName;

    private Date startAt;

    private Date endAt;

}
