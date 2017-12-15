package com.finance4car.repository;

import com.finance4car.domain.Finance4carModuleItem;
import com.finance4car.repository.example.Finance4carModuleItemExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Finance4carModuleItemMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    long countByExample(Finance4carModuleItemExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    int deleteByExample(Finance4carModuleItemExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    int insert(Finance4carModuleItem record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    int insertSelective(Finance4carModuleItem record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    List<Finance4carModuleItem> selectByExample(Finance4carModuleItemExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    Finance4carModuleItem selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    int updateByExampleSelective(@Param("record") Finance4carModuleItem record, @Param("example") Finance4carModuleItemExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    int updateByExample(@Param("record") Finance4carModuleItem record, @Param("example") Finance4carModuleItemExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    int updateByPrimaryKeySelective(Finance4carModuleItem record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_module_item
     * @generated 2017年10月17日 09:13:40
     */
    int updateByPrimaryKey(Finance4carModuleItem record);
}