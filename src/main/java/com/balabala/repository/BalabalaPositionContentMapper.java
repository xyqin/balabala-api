package com.balabala.repository;

import com.balabala.domain.BalabalaPositionContent;
import com.balabala.repository.example.BalabalaPositionContentExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaPositionContentMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    long countByExample(BalabalaPositionContentExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    int deleteByExample(BalabalaPositionContentExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    int insert(BalabalaPositionContent record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    int insertSelective(BalabalaPositionContent record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    List<BalabalaPositionContent> selectByExample(BalabalaPositionContentExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    BalabalaPositionContent selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    int updateByExampleSelective(@Param("record") BalabalaPositionContent record, @Param("example") BalabalaPositionContentExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    int updateByExample(@Param("record") BalabalaPositionContent record, @Param("example") BalabalaPositionContentExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    int updateByPrimaryKeySelective(BalabalaPositionContent record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_position_content
     * @generated 2018年1月9日 04:35:21
     */
    int updateByPrimaryKey(BalabalaPositionContent record);
}