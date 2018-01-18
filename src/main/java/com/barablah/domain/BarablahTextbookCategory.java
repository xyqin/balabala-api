package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahTextbookCategory extends AbstractEntity<Long> {

    private Long parentId;

    private String categoryName;

    private String path;

    private Integer position;

}