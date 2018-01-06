package com.balabala.web.response;

import lombok.Data;

@Data
public class HomeworkItemDto {

    private Long id;

    private String type;

    private String name;

    private String question;

    private String correct;

    private String image;

    private String answer;

}
