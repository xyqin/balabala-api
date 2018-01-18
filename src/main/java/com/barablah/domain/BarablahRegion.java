package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahRegion extends AbstractEntity<Long> {

    private Long parentId;

    private String regionName;

    private String path;

    private Integer position;

}
