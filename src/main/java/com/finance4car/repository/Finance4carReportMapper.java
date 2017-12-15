package com.finance4car.repository;

import com.finance4car.domain.Finance4carReport;
import com.finance4car.repository.example.Finance4carReportExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Finance4carReportMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    long countByExample(Finance4carReportExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByExample(Finance4carReportExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    int insert(Finance4carReport record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    int insertSelective(Finance4carReport record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    List<Finance4carReport> selectByExample(Finance4carReportExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    Finance4carReport selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExampleSelective(@Param("record") Finance4carReport record, @Param("example") Finance4carReportExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExample(@Param("record") Finance4carReport record, @Param("example") Finance4carReportExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKeySelective(Finance4carReport record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_report
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKey(Finance4carReport record);
}