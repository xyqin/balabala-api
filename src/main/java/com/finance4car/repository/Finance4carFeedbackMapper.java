package com.finance4car.repository;

import com.finance4car.domain.Finance4carFeedback;
import com.finance4car.repository.example.Finance4carFeedbackExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Finance4carFeedbackMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    long countByExample(Finance4carFeedbackExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    int deleteByExample(Finance4carFeedbackExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    int insert(Finance4carFeedback record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    int insertSelective(Finance4carFeedback record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    List<Finance4carFeedback> selectByExample(Finance4carFeedbackExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    Finance4carFeedback selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    int updateByExampleSelective(@Param("record") Finance4carFeedback record, @Param("example") Finance4carFeedbackExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    int updateByExample(@Param("record") Finance4carFeedback record, @Param("example") Finance4carFeedbackExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    int updateByPrimaryKeySelective(Finance4carFeedback record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_feedback
     * @generated 2017年11月15日 10:54:03
     */
    int updateByPrimaryKey(Finance4carFeedback record);
}