package com.barablah.domain;

import lombok.Data;

import java.util.Date;

@Data
public class ExtBarablahClass {
    private Long classId;
    private Long lessionId;
    private String lessonName;
    private String className;
    private Date startAt;
    private Date endAt;
    private String status;
}
