package com.finance4car.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class SavePlanActionsResponse {

    private List<PlanActionDto> actions = Lists.newArrayList();

}
