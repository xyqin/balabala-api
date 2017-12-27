package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaTextbookHomework extends AbstractEntity<Long> {

    private Long textbookId;

    private String homeworkName;

    private HomeworkType type;

    private String question;

    private String correct;

    private String image;

}