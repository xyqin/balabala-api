package com.finance4car.web.response;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ModuleDto {

    private Long id;

    private String name;

    private List<ModuleGroupDto> groups = Lists.newArrayList();

}
