package com.balabala.repository;

import com.balabala.domain.BalabalaClassMember;
import com.balabala.repository.example.BalabalaClassMemberExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaClassMemberMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    long countByExample(BalabalaClassMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByExample(BalabalaClassMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    int insert(BalabalaClassMember record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    int insertSelective(BalabalaClassMember record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    List<BalabalaClassMember> selectByExample(BalabalaClassMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    BalabalaClassMember selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExampleSelective(@Param("record") BalabalaClassMember record, @Param("example") BalabalaClassMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExample(@Param("record") BalabalaClassMember record, @Param("example") BalabalaClassMemberExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKeySelective(BalabalaClassMember record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_member
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKey(BalabalaClassMember record);
}