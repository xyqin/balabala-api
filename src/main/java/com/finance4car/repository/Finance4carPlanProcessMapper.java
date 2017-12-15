package com.finance4car.repository;

import com.finance4car.domain.Finance4carPlanProcess;
import com.finance4car.repository.example.Finance4carPlanProcessExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Finance4carPlanProcessMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    long countByExample(Finance4carPlanProcessExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByExample(Finance4carPlanProcessExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    int insert(Finance4carPlanProcess record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    int insertSelective(Finance4carPlanProcess record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    List<Finance4carPlanProcess> selectByExample(Finance4carPlanProcessExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    Finance4carPlanProcess selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExampleSelective(@Param("record") Finance4carPlanProcess record, @Param("example") Finance4carPlanProcessExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExample(@Param("record") Finance4carPlanProcess record, @Param("example") Finance4carPlanProcessExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKeySelective(Finance4carPlanProcess record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_plan_process
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKey(Finance4carPlanProcess record);
}