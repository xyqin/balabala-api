package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaTextbook extends AbstractEntity<Long> {

    private Long categoryId;

    private String textbookName;

}