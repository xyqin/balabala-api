package com.barablah.repository;

import com.barablah.domain.BarablahMemberLesson;
import com.barablah.repository.example.BarablahMemberLessonExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BarablahMemberLessonMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    long countByExample(BarablahMemberLessonExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    int deleteByExample(BarablahMemberLessonExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    int insert(BarablahMemberLesson record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    int insertSelective(BarablahMemberLesson record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    List<BarablahMemberLesson> selectByExample(BarablahMemberLessonExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    BarablahMemberLesson selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    int updateByExampleSelective(@Param("record") BarablahMemberLesson record, @Param("example") BarablahMemberLessonExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    int updateByExample(@Param("record") BarablahMemberLesson record, @Param("example") BarablahMemberLessonExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    int updateByPrimaryKeySelective(BarablahMemberLesson record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  barablah_member_lesson
     * @generated 2018年1月18日 06:52:57
     */
    int updateByPrimaryKey(BarablahMemberLesson record);
}