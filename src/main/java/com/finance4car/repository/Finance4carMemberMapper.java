package com.finance4car.repository;

import com.finance4car.domain.Finance4carMember;
import com.finance4car.repository.example.Finance4carMemberExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface Finance4carMemberMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    long countByExample(Finance4carMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByExample(Finance4carMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    int insert(Finance4carMember record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    int insertSelective(Finance4carMember record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    List<Finance4carMember> selectByExample(Finance4carMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    Finance4carMember selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExampleSelective(@Param("record") Finance4carMember record, @Param("example") Finance4carMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    int updateByExample(@Param("record") Finance4carMember record, @Param("example") Finance4carMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKeySelective(Finance4carMember record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  finance4car_member
     * @generated 2017年10月16日 10:39:52
     */
    int updateByPrimaryKey(Finance4carMember record);
}