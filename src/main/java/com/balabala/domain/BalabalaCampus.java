package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaCampus extends AbstractEntity<Long> {

    private Long regionId;

    private String campusName;

}
