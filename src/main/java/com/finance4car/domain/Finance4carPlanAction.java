package com.finance4car.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Finance4carPlanAction extends AbstractEntity<Long> {

    private Long planId;

    private String actionName;

    private String description;

    private String output;

    private String communication;

    private String resource;

    private String inCharge;

    private Date implementAt;

    private String startNote;

    private String improvement;

}
