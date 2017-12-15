package com.finance4car.domain.json;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class ModuleGroup {

    private String name;

    private List<ModuleItem> items = Lists.newArrayList();

}
