package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaCourse extends AbstractEntity<Long> {

    private Long categoryId;

    private Long textbookId;

    private String courseName;

    private Integer onlineLessons;

    private Integer onlineDuration;

    private Integer offlineLessons;

    private Integer offlineDuration;

    private BigDecimal commission;

}
