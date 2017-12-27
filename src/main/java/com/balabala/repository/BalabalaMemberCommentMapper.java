package com.balabala.repository;

import com.balabala.domain.BalabalaMemberComment;
import com.balabala.repository.example.BalabalaMemberCommentExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaMemberCommentMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    long countByExample(BalabalaMemberCommentExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    int deleteByExample(BalabalaMemberCommentExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    int insert(BalabalaMemberComment record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    int insertSelective(BalabalaMemberComment record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    List<BalabalaMemberComment> selectByExample(BalabalaMemberCommentExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    BalabalaMemberComment selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    int updateByExampleSelective(@Param("record") BalabalaMemberComment record, @Param("example") BalabalaMemberCommentExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    int updateByExample(@Param("record") BalabalaMemberComment record, @Param("example") BalabalaMemberCommentExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    int updateByPrimaryKeySelective(BalabalaMemberComment record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_comment
     * @generated 2017年12月28日 12:00:27
     */
    int updateByPrimaryKey(BalabalaMemberComment record);
}