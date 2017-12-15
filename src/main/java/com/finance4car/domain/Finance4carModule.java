package com.finance4car.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Finance4carModule extends AbstractEntity<Long> {

    private MemberLevel memberLevel;

    private String moduleName;

    private Integer position;

    private Boolean enabled;

}
