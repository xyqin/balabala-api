package com.barablah.web.response;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class HomeworkItemDto {

    private Long id;

    private Long textbookId;

    private String type;

    private String name;

    private String question;

    private String option;

    private String correct;

    private String image;

    private String video;

    @NotBlank
    private String answer;

}
