package com.finance4car.domain.json;

import com.finance4car.Utils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class ModuleItem {

    private Long id;

    private String unit;

    private String name;

    private String description;

    private String fixedBudgetType;

    private String fixedBudgetExpression;

    private boolean fixedBudgetPercent;

    private BigDecimal fixedBudgetValue = BigDecimal.ZERO;

    private String fixedImplementationType;

    private String fixedImplementationExpression;

    private boolean fixedImplementationPercent;

    private BigDecimal fixedImplementationValue = BigDecimal.ZERO;

    private String variableBudgetType;

    private String variableBudgetExpression;

    private boolean variableBudgetPercent;

    private BigDecimal variableBudgetValue = BigDecimal.ZERO;

    private String variableImplementationType;

    private String variableImplementationExpression;

    private boolean variableImplementationPercent;

    private BigDecimal variableImplementationValue = BigDecimal.ZERO;

    private String totalBudgetType;

    private String totalBudgetExpression;

    private boolean totalBudgetPercent;

    private BigDecimal totalBudgetValue = BigDecimal.ZERO;

    private String totalImplementationType;

    private String totalImplementationExpression;

    private boolean totalImplementationPercent;

    private BigDecimal totalImplementationValue = BigDecimal.ZERO;

    public void merge(ModuleItem item) {
        if (Objects.nonNull(item)) {
            if (!fixedBudgetPercent) {
                fixedBudgetValue = Utils.nullToZero(fixedBudgetValue).add(Utils.nullToZero(item.getFixedBudgetValue()));
            }

            if (!fixedImplementationPercent) {
                fixedImplementationValue = Utils.nullToZero(fixedImplementationValue).add(Utils.nullToZero(item.getFixedImplementationValue()));
            }

            if (!variableBudgetPercent) {
                variableBudgetValue = Utils.nullToZero(variableBudgetValue).add(Utils.nullToZero(item.getVariableBudgetValue()));
            }

            if (!variableImplementationPercent) {
                variableImplementationValue = Utils.nullToZero(variableImplementationValue).add(Utils.nullToZero(item.getVariableImplementationValue()));
            }

            if (!totalBudgetPercent) {
                totalBudgetValue = Utils.nullToZero(totalBudgetValue).add(Utils.nullToZero(item.getTotalBudgetValue()));
            }

            if (!totalImplementationPercent) {
                totalImplementationValue = Utils.nullToZero(totalImplementationValue).add(Utils.nullToZero(item.getTotalImplementationValue()));
            }
        }
    }

}
