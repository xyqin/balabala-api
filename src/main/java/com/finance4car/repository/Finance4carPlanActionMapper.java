package com.finance4car.repository;

import com.finance4car.domain.Finance4carPlanAction;
import com.finance4car.repository.example.Finance4carPlanActionExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Finance4carPlanActionMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    long countByExample(Finance4carPlanActionExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByExample(Finance4carPlanActionExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    int insert(Finance4carPlanAction record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    int insertSelective(Finance4carPlanAction record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    List<Finance4carPlanAction> selectByExample(Finance4carPlanActionExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    Finance4carPlanAction selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExampleSelective(@Param("record") Finance4carPlanAction record, @Param("example") Finance4carPlanActionExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExample(@Param("record") Finance4carPlanAction record, @Param("example") Finance4carPlanActionExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKeySelective(Finance4carPlanAction record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_action
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKey(Finance4carPlanAction record);
}