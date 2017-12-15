package com.finance4car.web.response;

import lombok.Data;

@Data
public class ModuleItemDto {

    private Long id;

    private String unit;

    private String name;

    private String description;

    private String fixedBudgetType;

    private String fixedBudgetExpression;

    private Boolean fixedBudgetPercent;

    private String fixedImplementationType;

    private String fixedImplementationExpression;

    private Boolean fixedImplementationPercent;

    private String variableBudgetType;

    private String variableBudgetExpression;

    private Boolean variableBudgetPercent;

    private String variableImplementationType;

    private String variableImplementationExpression;

    private Boolean variableImplementationPercent;

    private String totalBudgetType;

    private String totalBudgetExpression;

    private Boolean totalBudgetPercent;

    private String totalImplementationType;

    private String totalImplementationExpression;

    private Boolean totalImplementationPercent;

}
