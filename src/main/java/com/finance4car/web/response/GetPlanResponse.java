package com.finance4car.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class GetPlanResponse {

    private Long id;

    private String name;

    private String description;

    private String currentGoal;

    private String challengeGoal;

    private String situation;

    private String promotion;

    private String grossIncrease;

    private String inCharge;

    private String content;

    private String images;

    private List<PlanActionDto> actions = Lists.newArrayList();

    private List<PlanProcessDto> processs = Lists.newArrayList();

}
