package com.finance4car.domain.json;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ModuleTest {

    @Test
    public void testMerge() {
        // 月报表
        Module monthlyModule = new Module();
        monthlyModule.setId(1L);
        monthlyModule.setName("版块1");

        ModuleGroup monthlyGroup1 = new ModuleGroup();
        monthlyGroup1.setName("单元组1");

        ModuleItem monthlyItem1 = new ModuleItem();
        monthlyItem1.setId(1L);
        monthlyItem1.setName("指标项1");
        monthlyItem1.setFixedBudgetValue(BigDecimal.ONE);
        monthlyItem1.setFixedImplementationValue(BigDecimal.TEN);
        monthlyItem1.setVariableBudgetValue(BigDecimal.ONE);
        monthlyItem1.setVariableImplementationValue(BigDecimal.TEN);
        monthlyItem1.setTotalBudgetValue(BigDecimal.ONE);
        monthlyItem1.setTotalImplementationValue(BigDecimal.TEN);
        monthlyGroup1.getItems().add(monthlyItem1);

        monthlyModule.getGroups().add(monthlyGroup1);

        // 合并报表
        Module module = new Module();
        module.setId(1L);
        module.setName("版块1");

        ModuleGroup group1 = new ModuleGroup();
        group1.setName("单元组1");

        ModuleItem item1 = new ModuleItem();
        item1.setId(1L);
        item1.setName("指标项1");
        item1.setFixedBudgetValue(BigDecimal.ONE);
        item1.setFixedImplementationValue(BigDecimal.TEN);
        item1.setVariableBudgetValue(BigDecimal.ONE);
        item1.setVariableImplementationValue(BigDecimal.TEN);
        item1.setTotalBudgetValue(BigDecimal.ONE);
        item1.setTotalImplementationValue(BigDecimal.TEN);
        group1.getItems().add(item1);

        module.getGroups().add(group1);

        // 测试合并
        monthlyModule.merge(module);

        Assert.assertEquals(2, monthlyItem1.getFixedBudgetValue().intValue());
        Assert.assertEquals(20, monthlyItem1.getFixedImplementationValue().intValue());
        Assert.assertEquals(2, monthlyItem1.getVariableBudgetValue().intValue());
        Assert.assertEquals(20, monthlyItem1.getVariableImplementationValue().intValue());
        Assert.assertEquals(2, monthlyItem1.getTotalBudgetValue().intValue());
        Assert.assertEquals(20, monthlyItem1.getTotalImplementationValue().intValue());
    }

    @Test
    public void testMergeWhenItemNotMatch() {
        // 月报表
        Module monthlyModule = new Module();
        monthlyModule.setId(1L);
        monthlyModule.setName("版块1");

        ModuleGroup monthlyGroup1 = new ModuleGroup();
        monthlyGroup1.setName("单元组1");

        ModuleItem monthlyItem1 = new ModuleItem();
        monthlyItem1.setId(1L);
        monthlyItem1.setUnit("单元组1");
        monthlyItem1.setName("指标项1");
        monthlyItem1.setFixedBudgetValue(BigDecimal.ONE);
        monthlyItem1.setFixedImplementationValue(BigDecimal.TEN);
        monthlyItem1.setVariableBudgetValue(BigDecimal.ONE);
        monthlyItem1.setVariableImplementationValue(BigDecimal.TEN);
        monthlyItem1.setTotalBudgetValue(BigDecimal.ONE);
        monthlyItem1.setTotalImplementationValue(BigDecimal.TEN);
        monthlyGroup1.getItems().add(monthlyItem1);

        monthlyModule.getGroups().add(monthlyGroup1);

        // 合并报表
        Module module = new Module();
        module.setId(1L);
        module.setName("版块1");

        ModuleGroup group1 = new ModuleGroup();
        group1.setName("单元组1");

        ModuleItem item1 = new ModuleItem();
        item1.setId(2L);
        item1.setUnit("单元组1");
        item1.setName("指标项1");
        item1.setFixedBudgetValue(BigDecimal.ONE);
        item1.setFixedImplementationValue(BigDecimal.TEN);
        item1.setVariableBudgetValue(BigDecimal.ONE);
        item1.setVariableImplementationValue(BigDecimal.TEN);
        item1.setTotalBudgetValue(BigDecimal.ONE);
        item1.setTotalImplementationValue(BigDecimal.TEN);
        group1.getItems().add(item1);

        module.getGroups().add(group1);

        // 测试合并
        monthlyModule.merge(module);

        Assert.assertEquals(2, monthlyGroup1.getItems().size());
    }

    @Test
    public void testMergeWhenEmpty() {
        // 月报表
        Module monthlyModule = new Module();
        monthlyModule.setId(1L);
        monthlyModule.setName("版块1");

        // 合并报表
        Module module = new Module();
        module.setId(1L);
        module.setName("版块1");

        ModuleGroup group1 = new ModuleGroup();
        group1.setName("单元组1");

        ModuleItem item1 = new ModuleItem();
        item1.setId(1L);
        item1.setUnit("单元组1");
        item1.setName("指标项1");
        item1.setFixedBudgetValue(BigDecimal.ONE);
        item1.setFixedImplementationValue(BigDecimal.TEN);
        item1.setVariableBudgetValue(BigDecimal.ONE);
        item1.setVariableImplementationValue(BigDecimal.TEN);
        item1.setTotalBudgetValue(BigDecimal.ONE);
        item1.setTotalImplementationValue(BigDecimal.TEN);
        group1.getItems().add(item1);

        module.getGroups().add(group1);

        // 测试合并
        monthlyModule.merge(module);

        Assert.assertEquals(1, monthlyModule.getGroups().size());
    }

}
