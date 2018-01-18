package com.barablah.web.response;

import lombok.Data;

@Data
public class HomeworkItemDto {

    private Long id;

    private Long textbookId;

    private String type;

    private String name;

    private String question;

    private String correct;

    private String image;

    private String answer;

}