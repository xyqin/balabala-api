package com.barablah.web.response;

import lombok.Data;

@Data
public class TextbookDto {

    private Long id;

    private String name;

    private String question;

    private String option;

    private String correct;

    private String image;

    private String video;

}
