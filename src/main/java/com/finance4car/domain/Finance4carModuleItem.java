package com.finance4car.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Finance4carModuleItem extends AbstractEntity<Long> {

    private Long moduleId;

    private String itemName;

    private String description;

    private String unit;

    private Integer position;

    private Boolean enabled;

    private ModuleItemType fixedBudgetType;

    private String fixedBudgetExpression;

    private Boolean fixedBudgetPercent;

    private ModuleItemType fixedImplementationType;

    private String fixedImplementationExpression;

    private Boolean fixedImplementationPercent;

    private ModuleItemType variableBudgetType;

    private String variableBudgetExpression;

    private Boolean variableBudgetPercent;

    private ModuleItemType variableImplementationType;

    private String variableImplementationExpression;

    private Boolean variableImplementationPercent;

    private ModuleItemType totalBudgetType;

    private String totalBudgetExpression;

    private Boolean totalBudgetPercent;

    private ModuleItemType totalImplementationType;

    private String totalImplementationExpression;

    private Boolean totalImplementationPercent;

}
