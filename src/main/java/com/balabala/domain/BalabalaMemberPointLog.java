package com.balabala.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalabalaMemberPointLog extends AbstractEntity<Long> {

    private Long memberId;

    private Integer points;

    private PointType type;

}