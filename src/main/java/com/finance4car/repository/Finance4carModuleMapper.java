package com.finance4car.repository;

import com.finance4car.domain.Finance4carModule;
import com.finance4car.repository.example.Finance4carModuleExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Finance4carModuleMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    long countByExample(Finance4carModuleExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByExample(Finance4carModuleExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    int insert(Finance4carModule record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    int insertSelective(Finance4carModule record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    List<Finance4carModule> selectByExample(Finance4carModuleExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    Finance4carModule selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExampleSelective(@Param("record") Finance4carModule record, @Param("example") Finance4carModuleExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExample(@Param("record") Finance4carModule record, @Param("example") Finance4carModuleExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKeySelective(Finance4carModule record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKey(Finance4carModule record);
}