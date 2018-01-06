package com.balabala.repository;

import com.balabala.domain.BalabalaMemberPointLog;
import com.balabala.repository.example.BalabalaMemberPointLogExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaMemberPointLogMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    long countByExample(BalabalaMemberPointLogExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    int deleteByExample(BalabalaMemberPointLogExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    int insert(BalabalaMemberPointLog record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    int insertSelective(BalabalaMemberPointLog record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    List<BalabalaMemberPointLog> selectByExample(BalabalaMemberPointLogExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    BalabalaMemberPointLog selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    int updateByExampleSelective(@Param("record") BalabalaMemberPointLog record, @Param("example") BalabalaMemberPointLogExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    int updateByExample(@Param("record") BalabalaMemberPointLog record, @Param("example") BalabalaMemberPointLogExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    int updateByPrimaryKeySelective(BalabalaMemberPointLog record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_point_log
     * @generated 2018年1月6日 09:19:37
     */
    int updateByPrimaryKey(BalabalaMemberPointLog record);
}