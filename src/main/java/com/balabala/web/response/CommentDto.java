package com.balabala.web.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {

    private Long id;

    private String content;

    private Date createdAt;

}
