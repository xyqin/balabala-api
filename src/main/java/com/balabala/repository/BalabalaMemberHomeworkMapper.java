package com.balabala.repository;

import com.balabala.domain.BalabalaMemberHomework;
import com.balabala.repository.example.BalabalaMemberHomeworkExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaMemberHomeworkMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    long countByExample(BalabalaMemberHomeworkExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    int deleteByExample(BalabalaMemberHomeworkExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    int insert(BalabalaMemberHomework record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    int insertSelective(BalabalaMemberHomework record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    List<BalabalaMemberHomework> selectByExample(BalabalaMemberHomeworkExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    BalabalaMemberHomework selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    int updateByExampleSelective(@Param("record") BalabalaMemberHomework record, @Param("example") BalabalaMemberHomeworkExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    int updateByExample(@Param("record") BalabalaMemberHomework record, @Param("example") BalabalaMemberHomeworkExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    int updateByPrimaryKeySelective(BalabalaMemberHomework record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_homework
     * @generated 2017年12月28日 01:55:47
     */
    int updateByPrimaryKey(BalabalaMemberHomework record);
}