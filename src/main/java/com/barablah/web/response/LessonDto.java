package com.barablah.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class LessonDto {

    private Long id;

    private String name;

    private Date startAt;

    private int duration;

    private String thumbnail;

    private boolean prepared;

    private String status;

}
