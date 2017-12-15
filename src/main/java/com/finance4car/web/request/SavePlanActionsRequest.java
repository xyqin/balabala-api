package com.finance4car.web.request;

import com.finance4car.web.response.PlanActionDto;
import lombok.Data;

import java.util.List;

@Data
public class SavePlanActionsRequest {

    private List<PlanActionDto> actions;

}
