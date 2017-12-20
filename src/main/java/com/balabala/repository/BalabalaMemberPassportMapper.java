package com.balabala.repository;

import com.balabala.domain.BalabalaMemberPassport;
import com.balabala.repository.example.BalabalaMemberPassportExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaMemberPassportMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    long countByExample(BalabalaMemberPassportExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByExample(BalabalaMemberPassportExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    int insert(BalabalaMemberPassport record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    int insertSelective(BalabalaMemberPassport record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    List<BalabalaMemberPassport> selectByExample(BalabalaMemberPassportExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    BalabalaMemberPassport selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExampleSelective(@Param("record") BalabalaMemberPassport record, @Param("example") BalabalaMemberPassportExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExample(@Param("record") BalabalaMemberPassport record, @Param("example") BalabalaMemberPassportExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKeySelective(BalabalaMemberPassport record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_passport
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKey(BalabalaMemberPassport record);
}