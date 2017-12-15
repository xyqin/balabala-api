package com.finance4car.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class Finance4carMember extends AbstractEntity<Long> {

    private Long regionId;

    private MemberLevel level;

    private String memberName;

    private String password;

    private String phoneNumber;

    private String email;

    private String dealer;

    private String underGroup;

    private String brand;

    private String postalAddress;

    private Integer hallArea;

    private Date openAt;

}
