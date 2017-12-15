package com.finance4car.web.request;

import com.finance4car.web.response.PlanProcessDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SavePlanProcessesRequest {

    private List<PlanProcessDto> processes;

}
