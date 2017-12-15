package com.finance4car.repository;

import com.finance4car.domain.Finance4carRegion;
import com.finance4car.repository.example.Finance4carRegionExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Finance4carRegionMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    long countByExample(Finance4carRegionExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByExample(Finance4carRegionExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    int insert(Finance4carRegion record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    int insertSelective(Finance4carRegion record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    List<Finance4carRegion> selectByExample(Finance4carRegionExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    Finance4carRegion selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExampleSelective(@Param("record") Finance4carRegion record, @Param("example") Finance4carRegionExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExample(@Param("record") Finance4carRegion record, @Param("example") Finance4carRegionExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKeySelective(Finance4carRegion record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_region
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKey(Finance4carRegion record);
}