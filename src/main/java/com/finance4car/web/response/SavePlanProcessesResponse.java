package com.finance4car.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class SavePlanProcessesResponse {

    private List<PlanProcessDto> processes = Lists.newArrayList();

}
