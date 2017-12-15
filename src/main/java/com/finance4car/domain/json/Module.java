package com.finance4car.domain.json;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
public class Module {

    private Long id;

    private String name;

    private List<ModuleGroup> groups = Lists.newArrayList();

    public void merge(Module module) {
        if (Objects.nonNull(module)) {
            Map<Long, ModuleItem> thisItems = Maps.newHashMap();
            Map<Long, ModuleItem> thatItems = Maps.newHashMap();

            for (ModuleGroup thisGroup : groups) {
                for (ModuleItem thisItem : thisGroup.getItems()) {
                    thisItems.put(thisItem.getId(), thisItem);
                }
            }

            for (ModuleGroup thatGroup : module.getGroups()) {
                for (ModuleItem thatItem : thatGroup.getItems()) {
                    thatItems.put(thatItem.getId(), thatItem);
                }
            }

            for (Long key : thisItems.keySet()) {
                ModuleItem thisItem = thisItems.get(key);
                ModuleItem thatItem = thatItems.get(key);
                thisItem.merge(thatItem);
                thatItems.remove(key);
            }

            for (Long key : thatItems.keySet()) {
                ModuleItem newItem = thatItems.get(key);
                boolean groupFound = false;

                for (ModuleGroup thisGroup : groups) {
                    if (newItem.getUnit().equals(thisGroup.getName())) {
                        thisGroup.getItems().add(newItem);
                        groupFound = true;
                        break;
                    }
                }

                if (!groupFound) {
                    ModuleGroup newGroup = new ModuleGroup();
                    newGroup.setName(newItem.getUnit());
                    newGroup.getItems().add(newItem);
                    groups.add(newGroup);
                }
            }
        }
    }

}
