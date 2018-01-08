package com.balabala.web.response;

import lombok.Data;

@Data
public class LessonDto {

    private Long id;

    private String name;

    private int duration;

    private String thumbnail;

    private boolean prepared;

    private String status;

}
