package com.finance4car.repository;

import com.finance4car.domain.Finance4carPlan;
import com.finance4car.repository.example.Finance4carPlanExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Finance4carPlanMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    long countByExample(Finance4carPlanExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByExample(Finance4carPlanExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    int insert(Finance4carPlan record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    int insertSelective(Finance4carPlan record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    List<Finance4carPlan> selectByExample(Finance4carPlanExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    Finance4carPlan selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExampleSelective(@Param("record") Finance4carPlan record, @Param("example") Finance4carPlanExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExample(@Param("record") Finance4carPlan record, @Param("example") Finance4carPlanExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKeySelective(Finance4carPlan record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKey(Finance4carPlan record);
}