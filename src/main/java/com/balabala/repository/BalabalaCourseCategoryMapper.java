package com.balabala.repository;

import com.balabala.domain.BalabalaCourseCategory;
import com.balabala.repository.example.BalabalaCourseCategoryExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaCourseCategoryMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    long countByExample(BalabalaCourseCategoryExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByExample(BalabalaCourseCategoryExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    int insert(BalabalaCourseCategory record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    int insertSelective(BalabalaCourseCategory record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    List<BalabalaCourseCategory> selectByExample(BalabalaCourseCategoryExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    BalabalaCourseCategory selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExampleSelective(@Param("record") BalabalaCourseCategory record, @Param("example") BalabalaCourseCategoryExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExample(@Param("record") BalabalaCourseCategory record, @Param("example") BalabalaCourseCategoryExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKeySelective(BalabalaCourseCategory record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_course_category
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKey(BalabalaCourseCategory record);
}