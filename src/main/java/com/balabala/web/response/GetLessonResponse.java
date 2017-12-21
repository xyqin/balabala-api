package com.balabala.web.response;

import lombok.Data;

@Data
public class GetLessonResponse {

    private Long id;

    private String name;

    private String className;

    private String teacherName;

    private int duration;

    private String video;

}
