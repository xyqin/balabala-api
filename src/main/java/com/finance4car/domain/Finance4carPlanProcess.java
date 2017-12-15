package com.finance4car.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Finance4carPlanProcess extends AbstractEntity<Long> {

    private Long planId;

    private String description;

    private String grossIncrease;

    private String feedback;

    private String improvement;

    private Integer week;

    private Date startAt;

    private Date endAt;

}
