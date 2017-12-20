package com.balabala.repository;

import com.balabala.domain.BalabalaClassLesson;
import com.balabala.repository.example.BalabalaClassLessonExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaClassLessonMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    long countByExample(BalabalaClassLessonExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByExample(BalabalaClassLessonExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    int insert(BalabalaClassLesson record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    int insertSelective(BalabalaClassLesson record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    List<BalabalaClassLesson> selectByExample(BalabalaClassLessonExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    BalabalaClassLesson selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExampleSelective(@Param("record") BalabalaClassLesson record, @Param("example") BalabalaClassLessonExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExample(@Param("record") BalabalaClassLesson record, @Param("example") BalabalaClassLessonExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKeySelective(BalabalaClassLesson record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_class_lesson
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKey(BalabalaClassLesson record);
}