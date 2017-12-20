package com.balabala.repository;

import com.balabala.domain.BalabalaMember;
import com.balabala.repository.example.BalabalaMemberExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaMemberMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    long countByExample(BalabalaMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByExample(BalabalaMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    int insert(BalabalaMember record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    int insertSelective(BalabalaMember record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    List<BalabalaMember> selectByExample(BalabalaMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    BalabalaMember selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExampleSelective(@Param("record") BalabalaMember record, @Param("example") BalabalaMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExample(@Param("record") BalabalaMember record, @Param("example") BalabalaMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKeySelective(BalabalaMember record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKey(BalabalaMember record);
}