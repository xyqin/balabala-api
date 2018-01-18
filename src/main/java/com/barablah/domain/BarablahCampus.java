package com.barablah.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BarablahCampus extends AbstractEntity<Long> {

    private Long regionId;

    private String campusName;

}
