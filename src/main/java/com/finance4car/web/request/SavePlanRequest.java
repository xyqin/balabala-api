package com.finance4car.web.request;

import lombok.Data;

@Data
public class SavePlanRequest {

    private Long id;

    private String name;

    private String description;

    private String department;

    private String currentGoal;

    private String challengeGoal;

    private String situation;

    private String promotion;

    private String grossIncrease;

    private String inCharge;

    private String content;

    private String images;

}
