package com.finance4car.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Finance4carPlan extends AbstractEntity<Long> {

    private Long memberId;

    private String planName;

    private String description;

    private PlanDepartment department;

    private String currentGoal;

    private String challengeGoal;

    private String situation;

    private String promotion;

    private String grossIncrease;

    private String inCharge;

    private String content;

    private Boolean processModified;

    private Boolean actionModified;

    private String images;

}
