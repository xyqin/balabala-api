package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahCourse extends AbstractEntity<Long> {

    private Long categoryId;

    private Long textbookCategoryId;

    private String courseName;

    private Integer onlineLessons;

    private Integer onlineDuration;

    private Integer offlineLessons;

    private Integer offlineDuration;

    private BigDecimal commission;

}