package com.balabala.repository;

import com.balabala.domain.BalabalaMemberShippingAddress;
import com.balabala.repository.example.BalabalaMemberShippingAddressExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BalabalaMemberShippingAddressMapper {
    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    long countByExample(BalabalaMemberShippingAddressExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByExample(BalabalaMemberShippingAddressExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    int insert(BalabalaMemberShippingAddress record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    int insertSelective(BalabalaMemberShippingAddress record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    List<BalabalaMemberShippingAddress> selectByExample(BalabalaMemberShippingAddressExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    BalabalaMemberShippingAddress selectByPrimaryKey(Long id);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExampleSelective(@Param("record") BalabalaMemberShippingAddress record, @Param("example") BalabalaMemberShippingAddressExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    int updateByExample(@Param("record") BalabalaMemberShippingAddress record, @Param("example") BalabalaMemberShippingAddressExample example);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKeySelective(BalabalaMemberShippingAddress record);

    /**
     *  RudderFramework框架生成代码，请不要直接修改..
     *  balabala_member_shipping_address
     * @generated 2017年12月15日 06:50:09
     */
    int updateByPrimaryKey(BalabalaMemberShippingAddress record);
}