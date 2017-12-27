package com.balabala.repository;

import com.balabala.domain.BalabalaTextbookCategory;
import com.balabala.repository.example.BalabalaTextbookCategoryExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaTextbookCategoryMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    long countByExample(BalabalaTextbookCategoryExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    int deleteByExample(BalabalaTextbookCategoryExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    int insert(BalabalaTextbookCategory record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    int insertSelective(BalabalaTextbookCategory record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    List<BalabalaTextbookCategory> selectByExample(BalabalaTextbookCategoryExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    BalabalaTextbookCategory selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    int updateByExampleSelective(@Param("record") BalabalaTextbookCategory record, @Param("example") BalabalaTextbookCategoryExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    int updateByExample(@Param("record") BalabalaTextbookCategory record, @Param("example") BalabalaTextbookCategoryExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    int updateByPrimaryKeySelective(BalabalaTextbookCategory record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_textbook_category
     * @generated 2017年12月28日 12:00:27
     */
    int updateByPrimaryKey(BalabalaTextbookCategory record);
}