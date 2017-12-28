package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaTextbookCategory extends AbstractEntity<Long> {

    private Long parentId;

    private String categoryName;

    private String path;

    private Integer position;

}