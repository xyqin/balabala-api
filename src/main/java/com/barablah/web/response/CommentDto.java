package com.barablah.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {

    private Long id;

    private String teacher;

    private String teacherAvatar;

    private String content;

    private Date createdAt;

}
