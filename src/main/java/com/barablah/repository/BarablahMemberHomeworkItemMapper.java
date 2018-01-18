package com.barablah.repository;

import com.barablah.domain.BarablahMemberHomeworkItem;
import com.barablah.repository.example.BarablahMemberHomeworkItemExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BarablahMemberHomeworkItemMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    long countByExample(BarablahMemberHomeworkItemExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    int deleteByExample(BarablahMemberHomeworkItemExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    int insert(BarablahMemberHomeworkItem record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    int insertSelective(BarablahMemberHomeworkItem record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    List<BarablahMemberHomeworkItem> selectByExample(BarablahMemberHomeworkItemExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    BarablahMemberHomeworkItem selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    int updateByExampleSelective(@Param("record") BarablahMemberHomeworkItem record, @Param("example") BarablahMemberHomeworkItemExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    int updateByExample(@Param("record") BarablahMemberHomeworkItem record, @Param("example") BarablahMemberHomeworkItemExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    int updateByPrimaryKeySelective(BarablahMemberHomeworkItem record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_homework_item
     * @generated 2018年1月18日 06:52:57
     */
    int updateByPrimaryKey(BarablahMemberHomeworkItem record);
}