package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaTextbook extends AbstractEntity<Long> {

    private Long categoryId;

    private TextbookType type;

    private String textbookName;

    private String question;

    private String option;

    private String correct;

    private String image;

    private String video;

}