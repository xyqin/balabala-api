package com.balabala.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class CurrentLessonResponse {

    private Long id;

    private String name;

    private String teacherName;

    private Date startAt;

    private Date endAt;

    private String accid;

    private String token;

    private String room;

    private Date timestamp = new Date();

}
