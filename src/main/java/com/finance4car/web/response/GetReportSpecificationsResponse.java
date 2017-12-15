package com.finance4car.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class GetReportSpecificationsResponse {

    private List<ModuleDto> guestModules = Lists.newArrayList();

    private List<ModuleDto> silverModules = Lists.newArrayList();

    private List<ModuleDto> goldModules = Lists.newArrayList();

}
