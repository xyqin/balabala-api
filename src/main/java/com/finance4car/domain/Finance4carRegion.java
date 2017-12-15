package com.finance4car.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Finance4carRegion extends AbstractEntity<Long> {

    private Long parentId;

    private String regionName;

    private String path;

    private Integer position;

}
